# Jumia's country number validator app


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Contents</summary>
  <ol>
    <li><b>Project Summary</b></li>
    <li><b>Demo Screens</b></li>
    <li><b>How To Run</a>
    <ul>
        <li><b>Run A JAR File</b></li>
        <li><b>Run With Docker</b></li>
      </ul></li>
    <li><b>Project Structure</b></li>
    <li><b>Enhancements</b></li>
  </ol>
</details>

<!-- Project Summary -->
## Project Summary

Lists and categorize country phone numbers, it also offers possibility to filter phone numbers by country and their state whether they are valid or not.


<!-- Demo Screens -->
## Demo Screens

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


<!How to run -->
## How To Run

You can run the project by any of the two methods listed below: JAR file or using docker.

### JAR File

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
   
### Using Docker

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
   
 
