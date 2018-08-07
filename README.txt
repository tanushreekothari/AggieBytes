Restaurant Recommendation System (AggieBites) v0.2

**************************************************************************
SUMMARY
**************************************************************************
Life as a student is hard by itself with the assignments, quizzes and the likes, and on top of that, we as students (especially those who don't know how to cook) face another predicament of finding good food to eat. Students who are new to campus/ college station are mostly not aware of the various restaurants on/ off campus to satisfy their appetite. To address this situation, we propose a solution that will recommend restaurants to the students based on cuisine type/ budget/
ratings and reviews/ location/ hours/ parking available. It will also display restaurant menus, landmarks nearby for ease of navigation and predict restaurants for old users. We believe implementing this idea will not only ease the effort of restaurant search for students but also will help them to find the dining option of their preference and in proximity to their location.

**************************************************************************
CHANGES
**************************************************************************
version 0.5
- final release
- refactored the code to improve scalability and readability
- fixed minor issues to ensure the flow works as expected

version 0.4
- release candidate
- recommended restaurants to user based on their favorite cuisine type(s)
- enhanced registration authentication with multiple field validations
- added restaurant index for easier tracking of restaurants

version 0.3
- beta release
- user authentication
- provided search results based on user search history
- reorganized data layer

version 0.2
- alpha release
- completed the first primary use case (restaurant recommendations based on city and cuisine type)
- naive implementation of recommendations (filter and sorting)

version 0.1
- proof of architecture

**************************************************************************
SETUP
**************************************************************************
1. Run the following command in the home directory to grant write permission to the Mongo database instance:
> sudo chmod -R go+w /data/db
2. Navigate to the project directory and run “gradle build” in the terminal to build the project. Then run “gradle run” to start the program.
3. IMPORTANT, PLEASE READ - when prompted to enter the login credentials, use the sample credentials below:
	username: tan
	password: tanushree@10
**************************************************************************
OTHER NOTES
**************************************************************************
1. Try “College Station” or “Bryan” for city when prompted. As for now our program only supports Bryan - College Station metropolitan area, but we do plan to expand to other major Texas cities in the near future.

