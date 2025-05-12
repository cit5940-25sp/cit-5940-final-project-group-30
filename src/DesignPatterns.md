# Design Patterns in Our Project

In our project, we plan to implement two main design patterns to improve code organization, maintainability, and efficiency. These patterns are Model-View-Controller (MVC) and the Flyweight pattern.

1. Model-View-Controller (MVC)
MVC is a design pattern that divides an application into three interconnected parts:
- Model:
  Manages the core data and game logic
- View:
  Handles the display and presentation to the user
- Controller:
  Processes user inputs and coordinates updates between the Model and View.

Our Implementation:
- Model:
  Movie, MovieFlyweight, Player, GameState, MovieGraph
- View:
  ConsoleView, TextUI
- Controller:  
  GameController, inputHandler

In the Class Diagram:
The Model classes are grouped together, and the View and Controller classes are shown separately. This visual separation illustrates how MVC keeps our code modular and easier to maintain.

2. Flyweight Pattern
The Flyweight pattern is used to reduce memory usage by sharing common data across multiple objects. This is particularly useful when many objects have identical or similar properties.

Our Implementation:
- MovieFlyweight:
  Stores shared movie data such as actors, directors, and composers.
- Movie:
  Instead of storing duplicate information, each ‘Movie’ object references the shared data in ‘MovieFlyweight’.

In the Class Diagram:
The relationship between ‘Movie’ and ‘MovieFlyweight’ is clearly indicated, demonstrating that shared data is managed efficiently.

Summary:
- MVC keeps our application well-organized by separating data handling (Model), user interface (View), and user input/logic (Controller).  
- Flyweight allows us to efficiently manage shared movie data, reducing redundancy and saving memory.

These design patterns are clearly reflected in our class diagram, ensuring our design remains modular, maintainable, and efficient.
