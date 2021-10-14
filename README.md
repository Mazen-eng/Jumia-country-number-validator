# Jumia's country number validator app


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Contents</summary>
  <ol>
    <li><a href="#project-summary"><b>Project Summary</b></a></li>
    <li><a href="#design-decisions"><b>Design Decisions</b></a></li>    
    <li><a href="#project-structure"><b>Project Structure</b></a></li>    
    <li><a href="#tech-stack"><b>Tech Stack</b></a></li>    
    <li><a href="#enhancements"><b>Enhancements</b></a></li>
    <li><a href="#how-to-run"><b>How To Run</a>
    <ul>
        <li><a href="#jar-file"><b>Run A JAR File</b></a></li>
        <li><a href="#using-docker"><b>Run With Docker</b></a></li>
      </ul></li>
    <li><a href="#demo-screens"><b>Demo Screens</b></a></li>
  </ol>
</details>

<!-- Project Summary -->
## PROJECT SUMMARY

Lists and categorize country phone numbers, it also offers possibility to filter phone numbers by country and their state whether they are valid or not.

<!-- Design Decisions -->
## DESIGN DECISIONS

* Using `DTOs` to encapsulate the necessary data inside one object to reduce the overhead of multiple API calls from the frontend to get the required info.
* Abstracting different app components by depending on `interfaces` rather than `implementations`.

<!-- Project Structure -->
## PROJECT STRUCTURE

* Frontend
    * Home page (CountryNumbers component) that renders the content on the user's screen.
    * Also when the `CountryNumbers` component is first loaded it sends a request to the backend to retrieve the available countries list.
    * Service that are responsible for making the requests..

* Backend
    * Controllers : Classes that handle the requests and use the appropriate serving method.
      * CountryController: Responsible for the request of the list of available countries from the cache.
      * CustomerController: Responsible for the customer's related requests including filtration and pagination using the service layer.
    * Models: Represents the models used.
      * Customer
      * Country
    * Repository: This is the DAO in order to deal with the database.
    * DTO: Represents the Data transfer object used to encapsulate the information that is sent to the frontend.
    * Services: The service layer that is used to perform the logic for filtering the customers, accessing the repo, using the mapper to map to the DTO, and all the remaining funcionality.
    * Utils: Classes that helps in optimizing and extending the functionality.
      * Validator: Used to validate the customers' phone numbers.
      * Mapper: Used to map the list of customers into a list of customerDtos.
      * Constants: Used to contain constant values used throughout the application.
      * Cache: Used to cache country related info and operations.
    * Exceptions: Customized exceptions for the application's specific domain.
      * NoCustomersFoundException.
      * InvalidCountryNameException.
    * Application.properties: Holds the configurations for the application (Dealing with the database, server port , ... etc).
    * Tests: The tests for customerService, end-points test for the CountryController and CustomerController.

<!-- Tech Stack -->
## TECH STACK

* Frontend.
  * VueJs.
    * Vuetify.
  * Axios.
* Backend
  * Java (11).
  * Spring boot.
  * Spring Data JPA.
  * SQLite.
  * Lombok.
  * Google Guava.
  * Swagger (Springdoc-openapi).
  * H2 Database.

<!-- Enhancements -->
## ENHANCEMENTS

* Implement searcing functionalities.
* Improve the database design so it can be used to reduce the overhead done by the application for the validation which can be optimized by reducing the usage of the app's memory.
* Adding a security layer.
* Adding searching capabilities by using different parameters.

<!How to run -->
## How To Run

You can run the project by any of the two methods listed below: JAR file or using docker.

### JAR FILE

1. Clone the repo
   ```sh
   git clone https://github.com/Mazen-eng/Jumia-country-number-validator.git
   ```
2. In the project's root directory run 
   ```sh
   mvn clean package
   ```
   Or you can run the following command instead if you want to put the package in your local repo
   ```sh
   mvn clean install
   ```
3. Run the following command
   ```sh
   java -jar target/jumia-validator-app.jar
   ```
4. The app is now running and you can access it through the following url
   ```sh
   http://localhost:8090/
   ```
5. You can access swagger api documentation on the following URL
   ```sh
   http://localhost:8090/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
   ```
   
### USING DOCKER

1. Clone the repo
   ```sh
   git clone https://github.com/Mazen-eng/Jumia-country-number-validator.git
   ```
2. In the project's root directory run 
   ```sh
   mvn clean package
   ```
   Or you can run the following command instead if you want to put the package in your local repo
   ```sh
   mvn clean install
   ```
3. Still on the project's root directory run the following command 
   ```sh
   docker build -t validator-app .
   ```
   (Don't forget the "." at the end of the command)
4. Also at the same path (Project's root folder) run the following
   ```sh
   docker run --rm -it -p 8090:8090 validator-app
   
5. The app is now running and you can access it through the following url
   ```sh
   http://localhost:8090/
   ```
6. You can access swagger api documentation on the following URL
   ```sh
   http://localhost:8090/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
   ```
   
 <!-- Demo Screens -->
## DEMO SCREENS

### Home page (All customers)
![Home - All customers](/Demo/home.PNG)

### Filter by country only (Country: Morocco)
![Filter - By country](/Demo/filter_by_country.PNG)

### Filter by phone number validity state only (State: invalid)
![Filter - By state](/Demo/filter_by_state.PNG)

### Filter by country and phone number validity state (Country: Uganda, State: valid)
![Filter - By country&state](/Demo/filter_by_country_and_state.PNG)

### The list of countries used to filter by country
![List - of all countries](/Demo/filter_by_country_list.PNG)

### The list of phone number validity state used to filter by state
![List - of all phone number validity states](/Demo/filter_by_state_list.PNG)

### Items per page options list used in the pagination info
![List - of items per page](/Demo/items_per_page_list.PNG)

### Customers paginated by 10 customers per page (Page number 1)
![Paginated - 10 customers per page](/Demo/paginated_10_per_page.PNG)

### Customers paginated by 5 customers per page (Page number 7)
![Paginated - 5 customers per page and page number 7](/Demo/paginated_5_per_page_number_7.PNG)
