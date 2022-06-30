### My Architectural Design

#### Model
* Recipe has name, vegetarian flag, serving number,  ingredients, instruction text, 
* Ingredient has name, amount(double) and unit (string)
- - - -

#### Assumption

Multiple Recipes with same name are allowed. It is hard to give unique name to every recipe. In real life we see it a lot same recipe name with different ingredients.

- - - -
#### Solution

* No sql database model was chosen, since there is only relation between ingredients and recipe there is no limitation to stuck with relational database
* Same end point provided for save and update recipes. As long as valid id given save end point will allow user to update recipe as well, otherwise it will be considered as save new recipe

- - - -
#### Build and Run Application
This application require java 17 and mongo-db to be run.

You can also use mongo db that run on docker container.

To run mongo db on docker you need to have docker installed 

You can initialize mongo db on docker container by running
`docker-compose up`

After running the application you can access and test [REST APIS](http://localhost:9090/swagger-ui.html#/) over swagger.
- - - -
