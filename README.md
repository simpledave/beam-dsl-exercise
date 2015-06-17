# Beam DSL exercise
The requirement is that the business can create arbitrary combinations of rules to limit if a "Reward" can be issued.   

Rewards take the form of a DSL such as: 

```
total reward budget is 100000 and
(per beamer maximum cycles per day is 2 and
per beamer maximum cycles per week is 3 and 
per beamer total reward is 500)
```

## The exercise
Your task is to implement this requirement. There is scaffolding provided but feel free to refactor this. Feel free to use any external libraries you normally would.

There are a number of entry points you should pick the one you are most comfortable with:
* LimitParser: parse the DSL
* RewardLimiter: evaluate the DSL

If you prefer to work in Java use:
* JavaLimitParser
* JavaRewardLimiter

There are corresponding tests that should be made to pass:
* LimitParserTest
* RewardLimiterTest


We use gitflow as our branching strategy. Please create a new feature branch for your implementation. 
If you do not have gitflow installed you can use the maven jgitflow plugin: mvn jgitflow:feature-start 


### Bonus
Implement a DisjunctionLimit
