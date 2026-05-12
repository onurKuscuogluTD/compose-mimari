# Banking Compose Migration Sample

Bu proje, Fragment/XML kullanan bir Android uygulamada Compose'a kontrollu gecisi gosteren ornek bir bankacilik uygulamasidir. Odak UI gorselliginden cok MVVM + Clean Architecture + UDF, Fragment Navigation ve View/Compose birlikte yasama sinirlaridir.

## Ekranlar

- `HomeFragment`: tamamen XML ve ViewBinding kullanir. Legacy ana ekran senaryosunu temsil eder.
- `AccountsFragment`: Fragment `ComposeView` host eder ve tum ekran Compose ile cizilir.
- `TransferFragment`: XML layout icinde birden fazla `ComposeView` barindirir. Shared state tek `TransferViewModel` uzerinden akar.

## Mimari

- `data`: asset JSON okuma, DTO modelleri, mapper ve repository implementasyonu.
- `domain`: repository contract'i, domain modelleri ve use-case'ler.
- `presentation`: Fragment, ViewModel, UiState, UI mapper ve Compose ekran/section'lari.
- `di`: Hilt module binding ve provider tanimlari.

State asagi akar: `Repository -> UseCase -> ViewModel -> UiState -> Fragment/Route -> Screen`.
Event yukari cikar: `Screen/XML click -> callback -> Fragment/Route -> ViewModel veya Fragment Navigation`.

## Mock Veri

Gercek backend yoktur. `app/src/main/assets/banking_mock.json` dosyasi backend payload'u gibi okunur, Kotlin Serialization ile DTO'ya parse edilir ve mapper ile domain modele cevrilir.

## Calistirma

```bash
./gradlew test
./gradlew assembleDebug
./gradlew lintDebug
```

## GitHub

`local.properties`, Gradle cache ve build ciktilari `.gitignore` icinde tutulur. Ilk commit icin onerilen mesaj:

```text
Initial MVVM clean architecture Compose interop banking sample
```

## Notlar

- Navigation Compose kullanilmaz; gecisler Fragment Navigation Component uzerindendir.
- Compose `Screen` ve section fonksiyonlari `ViewModel`, `Fragment`, `Repository` veya `NavController` bilmez.
- XML + Compose karma ekranda state owner yalnizca `ViewModel` olur; `ComposeView` sadece render host olarak kalir.
