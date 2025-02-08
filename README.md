## The backend demo project
I use IDEA to build this project, and actually there is no Spring Boot 3.2.0 version choice currently in https://start.spring.io/. 
I've also tried alibaba's mirror and their webiste gives something around Spring Boot 2.8.X. So the demo is in 3.3.8.<br>
I used MySQL instead of H2 database, the structure and data building sql file is uploaded.<br>
There is also a test gif below, using Postman to check API.<br>
In the newest version hibernate, there is a bug so the hibernate core is excluded<br>
(https://stackoverflow.com/questions/79312305/springboot-database-jdbc-url-connecting-through-datasource-hikaridatasource-h).
![image](https://github.com/user-attachments/assets/92de081f-9358-45ac-83cf-aa59dfe9d5f2)
### Here is the thing, and the extra logics I added:
You cannot login when you have already login.<br>
You cannot logout when you are not logged in. <br>
![spring boot](https://github.com/user-attachments/assets/7354e0c7-2f60-4847-a6c2-5caac0b8433b)
