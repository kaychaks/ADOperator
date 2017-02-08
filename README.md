# Active Directory Operator (ADOperator)
---
Operations with Active Directory (AD). Currently, fetching profiles belonging to a specific group
. More operations in future.

### Overview
---
Creating a Knowledge Base(KB) for associates require fetching relevant stuff from AD to start with. 
Profiles are fetched with a predefined set of attributes and exported to a file in TSV format (as
 required by KB platform).
 
The code is written in a loosely coupled and purely functional approach so that it becomes easy 
to extend and add features.

### Pre-Requisite
---
- `sbt`
 
### Configuration
---
- `reference.conf` lists all the relevant configs required to run the application
- some of the values need to be updated like connection params to AD listed in `ad.login`, `search.base` & `search.group`
- to change the value of config params - either the specific option can be changed in `reference
.conf` itself or specific config params (with updated values) can be added to a new file 
`application.conf` (value of the props here will get higher priority). Later way is recommended
- configuration follows the [_HOCON_ spec](https://github.com/typesafehub/config/blob/master/HOCON.md)

### Usage
---
- once the relevant configs are updated, `sbt run` from inside the project directory should fetch
 the profiles and produce a TSV file of the name mentioned in 
 `output-files-loc` config
 
### Project Info
---
- `Profile` data type contains the structure of information fetched from AD and the same is then 
exported as TSV
- `State` abstraction is used to store intermediate values esp. `manager` profiles to minimise 
redundant AD calls
- `Show` type classes are used to produce TSV encoding
- dependencies - `pureconfig` for loading configuration files & `cats` for all typelevel goodness



