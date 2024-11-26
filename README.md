# VoteVault

## Description  
This project is a **Java-based Voting System** built using advanced Java concepts, Swing for the user interface, and MySQL for backend database operations. It provides functionalities for both **Admin** and **Voter** roles, allowing seamless management of elections, candidates, regions, and voting processes.  

The project follows an MVC (Model-View-Controller) architecture with DAO (Data Access Object) classes to handle database operations, ensuring modularity and scalability.  

## Features  
### Admin Dashboard  
- Manage Parties, Regions, and Candidates.  
- Election Management.  
- Voter Management.  
- View Results using bar charts and tables.  

### Voter Dashboard  
- Cast Vote securely.  
- View Election Results.  

### Security  
- Role-based access and password encryption.  
- Status validation for voting.  

## Technology Stack  
- **Frontend:** Java Swing.  
- **Backend:** MySQL.  
- **Libraries/Utilities:** JDBC, JFreeChart.  

## Installation  
1. **Prerequisites:**  
   - JDK 21 or above.  
   - Apache NetBeans IDE 22.  
   - MySQL 9.0.  

2. **Setup Instructions:**  
   - Clone or download the repository.  
   - Set up the MySQL database.  
   - Update database connection details in `DatabaseConnection.java` or `DBUtil.java`.  
   - Run the project using `VotingSystem.java`.  

## Usage  
- **Admin Login:**  
  Default: `admin/admin`.  
- **Voter Login:**  
  Register via admin panel and use those credentials to login.  

## Contributing  
Open to contributions via issues and pull requests.  

## License  
Licensed under the MIT License.  
