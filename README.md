# ShopifyChallenge
This is an Android Project for Shopify's Code Challenge.
Project Details:
This app will fetch the shop/app owner's inventory from the Shopify Server and display the list of products in the main screen. It also allows the user to open each product and see its details. In the detail view, it allows the user to switch between product variants and see its corresponding inventory quantity and prices etc.

Some fancy stuff I impelmented in this project:
1. Using Retrofit to handle API calls
2. Used ExecutorService to assigned a fixed size thread pool in order to downlaoding time-consuming images on the background simultaneously
3. Implemented my own Ram Cache to store product images, thus upon on resume the images are loaded from cache instead of the server, and the previous location is resumed for user when returning from the detail page.
4. Implemented Pull to Refresh funtion on RecyclerView, so the user can easily to refresh the data. 
5. Implemented a call back function when user switching between two pages to notify the main page reload data from the server, if the server data has been changed.
