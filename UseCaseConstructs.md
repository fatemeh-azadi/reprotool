Here, we are gathering possible constructs that appear in use-case specifications. They won't be necessarily supported by the ReProTool prototype. However, we should be aware of them.

Useful bibliography:
| Larman | Applying UML and Patterns: An Introduction to Object-Oriented Analysis and Design and Iterative Development (3rd Edition) |
|:-------|:--------------------------------------------------------------------------------------------------------------------------|
| Cockburn | Writing effective use-cases                                                                                               |

Table of contents:


# List of constructs #

## Scope of a use-case ##
Can be company, system or component.

## Interests of stakeholders ##
Some description about interests of all stakeholders. Most of the sentences are similar to _"Someone want something"_ e.g. _"Company want to accurately record transactions"_.

  * we could check if for all "wants" there is at least one requirement

## Preconditions/Postconditions ##
  * **precoditions** describe situation that should hold before the scenario can even begin and are **not tested** within the scenario.
  * should not describe obvious conditions
  * should not add "useless noise"
  * should describe some "alerts"

## References within steps ##
In some specifications, there are use-case steps that contain references to other use-case steps:
  * looping a range of steps: e.g. "If some condition holds, repeat steps 2-3"
  * conditional jump: e.g. "if something happens, continue with step 4"

## References to other use-cases ##
  * if some condition holds continue with use-case X

# Restrictions in Use-Case Templates #
The following list of restrictions is taken from a paper:
| Yue, Briand, Labiche | A Use Case Modeling Approach to Facilitate the Transition towards Analysis Models: Concepts and Empirical Evaluation, MODELS 2009 |
|:---------------------|:----------------------------------------------------------------------------------------------------------------------------------|

We think that the restrictions listed below can be checked or enforced automatically by our tool:
  * The subject of a sentence in basic and alternative flows should be the system or an actor.
  * The flow of events should be described sequentially
  * Actor-to-actor interactions are not allowed **(?!)**
  * One action per sentence (compound predicates should be avoided)
  * Use simple sentences only - one subject and one predicate
  * Only present tense should be used
  * Use active voice rather than passive voice
  * The interaction between the system and actors should be described without omitting its sender and receiver.
  * Only declarative sentences should be used (The sentence: "Is the system idle?" is non-declarative)
  * Use words in a consistent way, keep one term to describe one thing.
  * Modal verbs should not be used (e.g. _might_).  Modal verbs and adverbs usually indicate uncertainty; therefore metrics should be used if possible. **(?)**
  * Avoid adverbs (e.g. _very_)
  * Avoid negative adverbs and adjectives (e.g. _hardly_), but it is allowed to use _not_ or _no_
  * Do not use pronouns, avoid anaphora. (e.g. _he_, _this_)
  * Do not use participle phrases as adverbial modifier. For example: "ATM is idle, _displaying a Welcome message_"
  * Use "the system" to refer to the SuD consistently.