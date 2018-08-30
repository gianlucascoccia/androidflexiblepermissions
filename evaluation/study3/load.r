setwd("/Users/gianlucascoccia/Desktop/svn/gianluca/TSE_2018/evaluation/developers")

questions = read.csv2("data/Developers_survey_replies.csv", sep=",", header = TRUE, quote="\"")
colnames(questions) = c('submitDate', 'experience', 'appsDeveloped', 'membersCount',  'opionOnA6', 'CommentsOnA6', 'S1', 'S2', 'S3', 'S4', 'S5', 'S6', 'S7', 'S8', 'S9', 'S10', 'Q1', 'Q2', 'Q3', 'Contacts', 'Comment')

questions$Q1 <- ordered(questions$Q1,
                 levels = c("Absolutely no","No", "Don't know","Yes","Absolutely yes"))

questions$Q2 <- ordered(questions$Q2,
                 levels = c("Absolutely no","No", "Don't know","Yes","Absolutely yes"))

questions$Q3 <- ordered(questions$Q3,
                 levels = c("Absolutely no","No", "Don't know","Yes","Absolutely yes"))

questions$S1 <- ordered(questions$S1,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

questions$S2 <- ordered(questions$S2,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

questions$S3 <- ordered(questions$S3,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

questions$S4 <- ordered(questions$S4,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

questions$S5 <- ordered(questions$S5,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

questions$S6 <- ordered(questions$S6,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

questions$S7 <- ordered(questions$S7,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

questions$S8 <- ordered(questions$S8,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

questions$S9 <- ordered(questions$S9,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

questions$S10 <- ordered(questions$S10,
                   levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

getmode <- function(v) {
  uniqv <- unique(v)
  uniqv[which.max(tabulate(match(v, uniqv)))]
}

questions_freq_s1 = round(table(questions$S1) / 11, digits = 2)
questions_freq_s2 = round(table(questions$S2) / 11, digits = 2)
questions_freq_s3 = round(table(questions$S3) / 11, digits = 2)
questions_freq_s4 = round(table(questions$S4) / 11, digits = 2)
questions_freq_s5 = round(table(questions$S5) / 11, digits = 2)
questions_freq_s6 = round(table(questions$S6) / 11, digits = 2)
questions_freq_s7 = round(table(questions$S7) / 11, digits = 2)
questions_freq_s8 = round(table(questions$S8) / 11, digits = 2)
questions_freq_s9 = round(table(questions$S9) / 11, digits = 2)
questions_freq_s10 = round(table(questions$S10) / 11, digits = 2)

S1_norm = (as.numeric(questions$S1) -1)
S3_norm = (as.numeric(questions$S3) -1)
S5_norm = (as.numeric(questions$S5) -1)
S7_norm = (as.numeric(questions$S7) -1)
S9_norm = (as.numeric(questions$S9) -1)

S2_norm = (5 - as.numeric(questions$S2))
S4_norm = (5 - as.numeric(questions$S4))
S6_norm = (5 - as.numeric(questions$S6)) 
S8_norm = (5 - as.numeric(questions$S8)) 
S10_norm = (5 - as.numeric(questions$S10))

SUS_score = (sum(S1_norm + S2_norm + S3_norm + S4_norm + S5_norm + 
                   S6_norm + S7_norm + S8_norm + S9_norm + S10_norm) * 2.5)/11

S1n_freq = round(table(factor(S1_norm, levels = 0:4)) / 11, digits = 2)
S2n_freq = round(table(factor(S2_norm, levels = 0:4)) / 11, digits = 2)
S3n_freq = round(table(factor(S3_norm, levels = 0:4)) / 11, digits = 2)
S4n_freq = round(table(factor(S4_norm, levels = 0:4)) / 11, digits = 2)
S5n_freq = round(table(factor(S5_norm, levels = 0:4)) / 11, digits = 2)
S6n_freq = round(table(factor(S6_norm, levels = 0:4)) / 11, digits = 2)
S7n_freq = round(table(factor(S7_norm, levels = 0:4)) / 11, digits = 2)
S8n_freq = round(table(factor(S8_norm, levels = 0:4)) / 11, digits = 2)
S9n_freq = round(table(factor(S9_norm, levels = 0:4)) / 11, digits = 2)
S10n_freq = round(table(factor(S10_norm, levels = 0:4)) / 11, digits = 2)

foodel_features = read.csv2("data/foodel-results.csv", sep=";", header = FALSE, quote="\"")
identical_features = read.csv2("data/IdenticalFilesFinder-results.csv", sep=";", header = FALSE, quote="\"")
press_features = read.csv2("data/pressweaf-results.csv", sep=";", header = FALSE, quote="\"")
fivesec_features = read.csv2("data/5sec-results.csv", sep=";", header = FALSE, quote="\"")
ambiens_features = read.csv2("data/ambiens-results.csv", sep=";", header = FALSE, quote="\"")
electrosmart_features = read.csv2("data/electrosmart-results.csv", sep=";", header = FALSE, quote="\"")
gadfly_features = read.csv2("data/gadfly-results.csv", sep=";", header = FALSE, quote="\"")
knownmovies_features = read.csv2("data/knownmovies-results.csv", sep=";", header = FALSE, quote="\"")
mymensa_features = read.csv2("data/mymensa-results.csv", sep=";", header = FALSE, quote="\"")
peaklens_features = read.csv2("data/peaklens-results.csv", sep=";", header = FALSE, quote="\"")
ums_features = read.csv2("data/ums-results.csv", sep=";", header = FALSE, quote="\"")
yopapp_features = read.csv2("data/yopapp-results.csv", sep=";", header = FALSE, quote="\"")

options(scipen = 999)
foodel_time = (signif(max(foodel_features$V3), digits = 10)/1000 - signif(min(foodel_features$V3), digits = 10)/1000) / 60 
identical_time = (signif(max(identical_features$V3), digits = 10)/1000 - signif(min(identical_features$V3), digits = 10)/1000) / 60 
press_time = (signif(max(press_features$V3), digits = 10)/1000 - signif(min(press_features$V3), digits = 10)/1000) / 60 
fivesec_time = (signif(max(fivesec_features$V3), digits = 10)/1000 - signif(min(fivesec_features$V3), digits = 10)/1000) / 60 
ambiens_time = (signif(max(ambiens_features$V3), digits = 10)/1000 - signif(min(ambiens_features$V3), digits = 10)/1000) / 60 
electrosmart_time = (signif(max(electrosmart_features$V3), digits = 10)/1000 - signif(min(electrosmart_features$V3), digits = 10)/1000) / 60 
gadfly_time = (signif(max(gadfly_features$V3), digits = 10)/1000 - signif(min(gadfly_features$V3), digits = 10)/1000) / 60 
knownmovies_time = (signif(max(knownmovies_features$V3), digits = 10)/1000 - signif(min(knownmovies_features$V3), digits = 10)/1000) / 60 
mymensa_time = (signif(max(mymensa_features$V3), digits = 10)/1000 - signif(min(mymensa_features$V3), digits = 10)/1000) / 60 
peaklens_time = (signif(max(peaklens_features$V3), digits = 10)/1000 - signif(min(peaklens_features$V3), digits = 10)/1000) / 60 
ums_time = (signif(max(ums_features$V3), digits = 10)/1000 - signif(min(ums_features$V3), digits = 10)/1000) / 60 
yopapp_time = (signif(max(yopapp_features$V3), digits = 10)/1000 - signif(min(yopapp_features$V3), digits = 10)/1000) / 60 

times = c(foodel_time, identical_time, press_time, fivesec_time, ambiens_time, electrosmart_time,
          gadfly_time, knownmovies_time, mymensa_time, peaklens_time, 
          ums_time, yopapp_time)

