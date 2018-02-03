# Team Treehouse Project 5 - Countries of the World

## Summary

Project which was built as part of Team Treehouse Techdegree. Using Hibernate to
interact with a pre-populated database of countries and their corresponding 
number of internet users and adult literacy rate. The project allows the user
to add new countries and edit/delete existing countries.

Features:
1. Displaying all data within the database
2. Showing statistics for each country in the table and a correlation coefficient
3. Adding a country to the database
4. Editing a country in the database
5. Deleting a country in the database

## Assumptions

For calculation of the correlation coefficient standard deviation was used. Assumption
was that population standard deviation formula was used and not sample. 

For the EDIT feature, only the country name, internet users and adult literacy rate
can be edited. 


## Built With

org.hibernate:hibernate-core:5.1.0.Final
com.h2database:h2:1.4.191
javax.transaction:jta:1.1

## Authors

Ross Logan

## Acknowledgements and References

Team Treehouse Java Web Development Tech Degree Community

Calculation of Correlation Coefficient:
https://www.thoughtco.com/how-to-calculate-the-correlation-coefficient-3126228

For calculation of standard deviation I have used the "population" standard deviation
formula. For more details on both types of standard deviation please see the following link:
https://statistics.laerd.com/statistical-guides/measures-of-spread-standard-deviation.php