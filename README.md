# **Swipe Assignment(YC S21) - Product List App**

This was an assignment by Swipe (YC S21). The app lets you view a product list and add your own with an image, name, type, price, and tax. It supports Offline First, allowing you to add products without the internet; they are stored locally and synced once the connection is restored.

## **Features**

#### **Products list**
  - You can see a list of products, in that you can see product's image, name, type, it's price and tax percentage.
  - When you click on the image of a product(only those who has an image) you can see the full image and you can also zoom-in or zoom-out on that image.
#### **Search bar**
  - You can search a product either by it's name or it's type.
  - If there is no such product it shows that "Product does not exists".
  - When you scroll up the search bar hides smoothly and when you scroll down it shows itself.
#### **Add Product**
  - You can simply add a product by giving some information like name, type(you can select a type from the given list), price and tax, image is optional.
  - The "Add Product" button will be only enabled when you fill all the fields except image.
  - When you click on the "Add Product" button, a circular progress indicator will be shown in place of the button as it's making an API call.
#### **Offline first & Notification**
  - Even if you don't have network you can still add a product, first it will be stored manually and when the network is available, it will be deleted from your local storage and gets added to the list of products.
  - When the prodcut is added successfully you will see a notification, that the product is added successfully

--- 

## **Demo**

<video src="https://github.com/user-attachments/assets/dd697539-aebd-42ff-85c5-f9488814058a" controls="controls" style="max-width: 100%; height: auto;">
    Demo how the app works.
</video>

--- 
  
## **Libraries and Methods Used**
1. **Kotlin**: First-class and official programming language for Android development.
2. **Jetpack compose**: A toolkit for building Android apps that uses a declarative API to simplify and speed up UI development
3. **Material Components for Android**: For modular and customizable Material Design UI components.
4. **Clean Architecture**: It is a design approach that separates an application into layers (e.g., presentation, domain, data) to ensure scalability, maintainability, and independence from frameworks or external libraries.
5. **Kotlin Coroutines**: They are a concurrency framework that simplifies asynchronous programming by allowing tasks to be written sequentially while managing threading and suspensions efficiently.
6. **Room**: A persistence library that provides an abstraction layer over SQLite to manage local database operations efficiently in Android applications.
7. **Koin**: It is a lightweight dependency injection framework for Kotlin, enabling easy and modular DI setup.
8. **Retrofit**: It is a type-safe HTTP client for Kotlin and Android used to make API requests and handle responses efficiently.
9. **Serialization**: It is a library for converting Kotlin objects to and from JSON, making data parsing and handling seamless.
10. **Coil**: It is a lightweight and fast image loading library for Android that supports Jetpack Compose and leverages Kotlin coroutines.
11. **Work Manager**: It is an Android library for scheduling and managing deferrable, guaranteed background tasks efficiently. Used to store products locally if there is not network and then add to the API when there is network.
12. **Splash API**: In Android it provides a smooth startup experience by showing a launch screen while the app initializes.
13. **Accompanist**: It is a set of Jetpack Compose libraries that provide utilities for animations, permissions, navigation, and more. I mainly used this for FlowRow.
    
--- 

## Lessons Learned

While building this app, I learned about:



--- 

## **Contact**
For any questions or feedback, feel free to contact me at sakhare1181likhit@gmail.com and also connect with me on LinkedIn at www.linkedin.com/in/likhit-sakhare and on Twitter at https://x.com/likhit_sakhare
