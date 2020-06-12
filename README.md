# GithubUsers
This sample app uses [Github Search API](https://developer.github.com/v3/search/#search-users) to search for users and display user details.
A user can be removed from the search results by swiping horizontally, while dragging users up and down will reorder the search result list.

- It is written in **Kotlin**, using **Coroutines**
- The Presentation layer is implemented using Model-View-ViewModel (**MVVM**) and **LiveData**
- The Data layer is implemented with **Retrofit** and in-memory cache and masked behind a **Repository**
- **Dagger 2** is used for dependency injection.
- **Junit4** and **mockk** are used for testing
- [ktlin](https://github.com/pinterest/ktlint) is used for static code analysis
