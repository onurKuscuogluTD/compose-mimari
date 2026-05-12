# Fragment/XML + Compose Migration Şemasının MVI Codebase Uygulaması

Bu doküman, Fragment/XML + Compose migration şemasındaki kararların bu örnek bankacılık uygulamasının MVI branch'inde nasıl uygulandığını anlatır. Odak nokta UI tasarımı değil; Fragment Navigation, XML/Compose birlikte yaşama modeli, ViewModel kaynaklı state, intent/reducer/effect akışı ve UDF sınırlarının codebase içinde nasıl konumlandığıdır.

## Ana Prensiplerin Codebase Karşılığı

- Fragment-based Navigation korunur: geçişler `app/src/main/res/navigation/nav_graph.xml` üzerinden yapılır.
- Shared screen state ViewModel'den gelir: `HomeViewModel`, `AccountsViewModel`, `TransferViewModel`.
- UI event'leri ViewModel'e intent olarak gider: `HomeIntent`, `AccountsIntent`, `TransferIntent`.
- Navigation gibi one-off işler effect olarak dışarı çıkar: `HomeEffect`, `AccountsEffect`, `TransferEffect`.
- XML ve Compose birlikte yaşayabilir: `TransferFragment`, XML layout içinde birden fazla `ComposeView` host eder.
- Full Compose ekran Fragment host ile çalışabilir: `AccountsFragment`, `BaseComposeFragment` üzerinden Compose content döner.
- Binding lifecycle standardı merkezileştirilir: XML ve mixed ekranlar `BaseViewBindingFragment` ve `withBinding {}` kullanır.

## Migration Kararı -> Codebase Mapping

| HTML şemasındaki karar | Codebase karşılığı | Neden böyle uygulandı? |
| --- | --- | --- |
| Full XML ekran korunabilir | `HomeFragment` + `fragment_home.xml` | Legacy başlangıç ekranını temsil eder; Compose migration zorunlu olarak tüm ekranlardan aynı anda başlamaz. |
| XML içinde küçük Compose parçaları kullanılabilir | `TransferFragment` + `fragment_transfer.xml` içindeki `ComposeView` alanları | XML ekran iskeleti korunurken hesap seçimi, alıcı listesi ve CTA gibi parçalar Compose ile taşınır. |
| Fragment kalır, ekran tamamen Compose olur | `AccountsFragment` + `BaseComposeFragment` + `AccountsRoute` | Mixed dönemde Fragment container/navigation bridge olarak kalır; UI akışı Compose Route/Screen yapısına taşınır. |
| Shared state Fragment'ta tutulmaz | `TransferViewModel` -> reducer -> `TransferUiState` -> XML/Compose render | XML ve Compose aynı ekran state'ini ayrı ayrı sahiplenmez; source of truth ViewModel olur. |
| UI aksiyonları intent'e çevrilir | `TransferIntent.AccountSelected`, `AccountsIntent.TransferClicked`, `HomeIntent.AccountsClicked` | UI katmanı ViewModel metot detayını bilmez; tek giriş noktası `onIntent(...)` olur. |
| Navigation one-off effect'tir | `HomeEffect`, `AccountsEffect`, `TransferEffect` | Navigation state içinde tutulmaz ve ViewModel'e `NavController` girmez. |
| Fragment binding lifecycle'ı standartlaştırılır | `BaseViewBindingFragment` + `withBinding {}` | `_binding` tekrarını feature fragment'lardan çıkarır ve binding erişimini view lifecycle aralığına sınırlar. |
| Full Compose Fragment host standardı merkezileştirilir | `BaseComposeFragment` | `ComposeView` ve `DisposeOnViewTreeLifecycleDestroyed` tekrarı tek yerde yönetilir. |

## Bu Branch'te MVVM'den Fark Ne?

- UI, feature-specific public ViewModel metotları yerine `onIntent(...)` kullanır.
- State değişimi `reduce { copy(...) }` üzerinden yapılır.
- Navigation, snackbar veya benzeri tek seferlik işler `Effect` olarak yayımlanır ve Fragment/Route tarafından tüketilir.
- `StateFlow<UiState>` korunur; değişen şey event giriş modeli ve one-off event teslim modelidir.

## 1. Mevcut Navigation Akışı

```mermaid
flowchart TD
    Activity["MainActivity"]
    Container["FragmentContainerView\nactivity_main.xml"]
    NavGraph["nav_graph.xml\nFragment Navigation"]
    Home["HomeFragment\nFull XML"]
    Accounts["AccountsFragment\nFragment + full Compose"]
    Transfer["TransferFragment\nXML + ComposeView sections"]

    Activity --> Container
    Container --> NavGraph
    NavGraph --> Home
    Home -->|"Hesaplarımı Gör"| Accounts
    Home -->|"Para Transferi"| Transfer
    Accounts -->|"Para Transferi"| Transfer
    Accounts -->|"Geri"| Home
    Transfer -->|"Geri"| Home
```

Bu akışta Navigation Compose kullanılmaz. Mixed View/Compose dönemi için Fragment Navigation ana yönlendirme katmanı olarak kalır. Compose ekranlar navigation kararını kendisi vermez; Fragment callback olarak dışarıdan bağlar.

## 2. `TransferFragment`: Fragment + XML + Compose

```mermaid
flowchart TD
    Fragment["TransferFragment\nBaseViewBindingFragment"]
    Xml["fragment_transfer.xml"]
    Toolbar["XML toolbar/title/status"]
    SourceCV["sourceAccountComposeView"]
    RecipientsCV["recipientsComposeView"]
    SummaryCV["transferSummaryComposeView"]
    VM["TransferViewModel"]
    State["TransferUiState"]
    SourceSection["SourceAccountSection"]
    RecipientSection["RecentRecipientsSection"]
    SummarySection["TransferSummarySection"]
    Intent["TransferIntent"]
    UseCase["GetTransferInitialDataUseCase\nSubmitTransferUseCase"]
    Repository["BankingRepository"]

    Fragment -->|"inflate binding"| Xml
    Xml --> Toolbar
    Xml --> SourceCV
    Xml --> RecipientsCV
    Xml --> SummaryCV

    Repository --> UseCase
    UseCase --> VM
    VM --> State

    State -->|"lifecycle-aware collect"| Fragment
    Fragment -->|"XML render"| Toolbar
    SourceCV --> SourceSection
    RecipientsCV --> RecipientSection
    SummaryCV --> SummarySection

    State --> SourceSection
    State --> RecipientSection
    State --> SummarySection

    SourceSection -->|"onAccountSelected"| Intent
    RecipientSection -->|"onRecipientSelected"| Intent
    SummarySection -->|"onSubmitClick"| Intent
    Intent -->|"onIntent"| VM
```

`TransferFragment` bu projedeki XML + Compose birlikte yaşama örneğidir. XML layout ekran iskeletini taşır; `ComposeView` alanları sadece belirli UI parçalarını render eder. Fragment, `TransferViewModel` state'ini toplar ve Compose section'lara yalnızca state/callback verir. Callback'ler Fragment içinde `TransferIntent`'e çevrilir. Compose section'lar Fragment, Repository veya NavController bilmez.

Bu model özellikle mevcut XML ekranların parça parça Compose'a taşınacağı migration süreci için uygundur. State owner yalnızca ViewModel'dir; XML text alanı ve Compose section'lar aynı `TransferUiState` üzerinden güncellenir. Navigation gibi tek seferlik işler `TransferEffect` ile Fragment'a döner.

## 3. `AccountsFragment`: Fragment + Full Compose

```mermaid
flowchart TD
    Fragment["AccountsFragment"]
    BaseCompose["BaseComposeFragment"]
    ComposeView["ComposeView\nDisposeOnViewTreeLifecycleDestroyed"]
    Theme["BankingTheme"]
    Route["AccountsRoute"]
    VM["AccountsViewModel"]
    Intent["AccountsIntent"]
    State["AccountsUiState"]
    Effect["AccountsEffect"]
    Screen["AccountsScreen"]
    Nav["Fragment NavController"]

    Fragment --> BaseCompose
    BaseCompose --> ComposeView
    ComposeView --> Theme
    Theme --> Route
    Route -->|"hiltViewModel"| VM
    Route -->|"callbacks -> intent"| Intent
    Intent -->|"onIntent"| VM
    VM -->|"reducer"| State
    VM --> Effect
    Route -->|"collectAsStateWithLifecycle"| State
    Route -->|"collect effect"| Effect
    Route --> Screen
    State --> Screen
    Screen -->|"onBackClick / onTransferClick"| Route
    Effect -->|"navigation callback"| Fragment
    Fragment --> Nav
```

`AccountsFragment` ekranın tamamının Compose'a taşındığı senaryoyu gösterir. Fragment burada UI sahibi değildir; container, lifecycle ve navigation bridge rolündedir. `AccountsRoute`, ViewModel alma, lifecycle-aware state collection, callback'leri `AccountsIntent`'e çevirme ve `AccountsEffect` toplama sınırıdır. `AccountsScreen` yalnızca `uiState + callbacks` alır; ViewModel, Fragment veya NavController bilmez.

`BaseComposeFragment`, full Compose Fragment host'larda aynı lifecycle stratejisinin tekrar tekrar yazılmasını engeller. Bu sayede feature fragment sadece kendi içeriğini ve navigation callback'lerini tanımlar.

## 4. Base Fragment Lifecycle Standardı

```mermaid
flowchart LR
    subgraph ViewBinding["BaseViewBindingFragment"]
        Inflate["onCreateView\ninflateBinding(...)"]
        Store["binding saklanır"]
        Access["withBinding { ... }"]
        Clear["onDestroyView\nbinding = null"]
        Inflate --> Store --> Access --> Clear
    end

    subgraph Compose["BaseComposeFragment"]
        CreateCV["onCreateView\nComposeView(requireContext())"]
        Strategy["DisposeOnViewTreeLifecycleDestroyed"]
        Content["setContent { Content() }"]
        CreateCV --> Strategy --> Content
    end
```

`BaseViewBindingFragment`, XML ve mixed ekranlarda binding lifecycle'ını feature fragment'lardan çıkarır. `withBinding {}` kullanımı, binding erişimini view lifecycle aralığında tutar ve fragment içinde `_binding` field tekrarını kaldırır.

`BaseComposeFragment`, full Compose host fragment'larda `ComposeView` oluşturma ve composition dispose stratejisini tek yerde standartlaştırır. Bu standardizasyon, migration örneğinde fragment'ların asıl sorumluluklarını daha görünür hale getirir.

## 5. UDF Veri Akışı

```mermaid
flowchart TD
    DataSource["BankingLocalDataSource\nassets/banking_mock.json"]
    Repository["BankingRepositoryImpl"]
    UseCase["UseCase"]
    Intent["Intent"]
    ViewModel["ViewModel"]
    Reducer["Reducer\nreduce { copy(...) }"]
    UiState["UiState"]
    Effect["Effect"]
    RouteOrFragment["Route / Fragment"]
    XML["XML Views"]
    Compose["Compose Screen / Sections"]
    User["User Action"]
    Callback["Callback"]

    DataSource --> Repository
    Repository --> UseCase
    UseCase --> ViewModel
    ViewModel --> Reducer
    Reducer -->|"StateFlow<UiState>"| UiState
    UiState --> RouteOrFragment
    RouteOrFragment --> XML
    RouteOrFragment --> Compose

    User --> Callback
    Callback --> RouteOrFragment
    RouteOrFragment --> Intent
    Intent -->|"onIntent"| ViewModel
    ViewModel -->|"one-off"| Effect
    Effect --> RouteOrFragment
    RouteOrFragment -->|"navigation effect"| Nav["Fragment Navigation"]
```

State aşağı akar: data source'tan repository ve use-case üzerinden ViewModel'e, oradan reducer ile `UiState` olarak UI katmanına gelir. Event yukarı çıkar: XML click veya Compose callback, Fragment/Route sınırında intent'e çevrilir. Business event ViewModel'de state'e indirgenir; navigation event `Effect` olarak Fragment Navigation'a bağlanır.

Bu ayrım özellikle mixed XML + Compose ekranlarda önemlidir. XML view ve Compose section aynı ViewModel state'ini render eder; ikisi de kendi başına source of truth olmaz.

## Code Review Kontrol Noktaları

- Fragment içinde `_binding` field'ı olmamalı; XML/mixed ekranlar `BaseViewBindingFragment` kullanmalı.
- Binding erişimi `withBinding {}` içinde kalmalı.
- Full Compose ekranlarda `ComposeView` ve composition strategy feature fragment içinde tekrar yazılmamalı.
- Compose `Screen` veya section fonksiyonları `Fragment`, `NavController`, `Repository` veya XML binding bilmemeli.
- UI event'leri feature-specific public ViewModel metotları yerine `onIntent(...)` ile gönderilmeli.
- Navigation event'leri state'e yazılmamalı; `Effect` olarak Fragment/Route tarafından tüketilmeli.
- XML + Compose karma ekranda state owner tek olmalı: ViewModel.
