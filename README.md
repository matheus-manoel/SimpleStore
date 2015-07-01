Simple Store Simulator
======
**Simple Store Simulator** is a software that simulates a server/client store application. 

#### Motivation
Made for our Object-oriented programming class (https://docs.google.com/document/d/1hZco9xbu2Q6F3rICbgalWUufmuWPvREZUmELk1MDTDs/edit).

## Usage
We were not able to make a jar. 

Import the project in Eclipse and execute the following files with their arguments:

1) StoreServer $port

2) Manager $ip $port

3) Client $ip $port

Obs.: Run as many Clients as you want.


## Contributors
Matheus Manoel, [Gabriel Luiz](https://github.com/gabrielludy) and Matheus Macedo.

### How does it work and what does it do?
StoreServer creates a ServerSocket and waits for the Manager to connect. The Mannager app has the power to add products, list products, set a new quantity to a chosen product and generate dynamic pdf reports. After the Manager connection, the server waits for Client connections. The Client app has the power to register new users, login, list products and buy them.

All the Clients and the Manager have their thread that listens to the requests (JSON was used here), parse this requests and send to the Controller classes (ManagerStoreController and ClientStoreController) to execute. The controller classes return any possible errors to the threads, and then this error finally arrives in the Client/Manager class, where the request came from.

### Libs used
We use JSON as an external library. In the package helper there are some essential classes as well: ErrorConstants (self explanatory) and MD5, a class made for the password encryption.

### Design Choices
In this project we use both Singleton and MVC patterns. The Store class is needed to coordinate actions across the
system, so only one instance of it is necessary. The MVC architectural pattern gave us more flexibility and made our code
really easy to follow: the view package has only the needed interfaces; the model package has the database classes, like
Product, User and Store; the controller package works like a bridge between the other two.

### Extra points
Pdf report.

Used design patterns and described it above.
