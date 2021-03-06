# Replication package for "Enhancing Trustability of Android Applications via Flexible Permissions"
This git repository contains the replication package for our TSE paper "Enhancing Trustability of Android Applications via Flexible Permissions".

### Abstract: 
> The Android OS market is experiencing a growing share globally, and it is becoming the mobile platform of choice for an increasing number of users. People rely on Android mobile devices for surfing the web, purchasing products, or to be part of a social network. The large amount of personal information that is exchanged makes privacy an important concern. As a result, the trustability of mobile apps is a fundamental aspect to be considered, particularly with regard to  meeting the expectations of end users. The rigidities of the Android permission model confine end users into a secondary role, offering the only option of choosing between either privacy or functionalities. 
The proposed approach empowers end users to selectively grant permission by specifying (i) the desired level of permissions granularity and (ii) the specific features of the app in which the chosen permission levels are granted. Four experiments have been designed, conducted, and reported for evaluating it. The experiments give promising results in terms of performance, usability, and acceptance from both the end user's and developer's perspective.

Authors:
* Gian Luca Scoccia: gianluca.scoccia/at/gssi.it
* Ivano Malavolta: i.malavolta/at/vu.nl
* Marco Autili: marco.autili/at/univaq.it
* Amleto Di Salle: amleto.disalle/at/univaq.it
* Paola Inverardi:paola.inverardi/at/univaq.it  

Structure of the replication package is as follows:

## Evaluation folder

It contains four directories. Each contains all the data and the analysis scripts used during each of the four studies described in the paper.

## Implementation folder

Contains both binaries and source code files for all parts of AFP. More in detail:

* *binary* folder: contains all the binaries for the AFP Instrumenter, that can be executed by running the *run.py* file. 

* *Mapping tool.zip*: contains all the source code files for the web-based mapping tool provided to developers to create features-components mappings.

* *instrumenterSources folder*: contains all the source code files for the AFP Library, the AFP Instrumenter and the AFP App.

