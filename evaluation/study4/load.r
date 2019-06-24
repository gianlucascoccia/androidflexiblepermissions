library(dplyr)
setwd("/Users/gianlucascoccia/Desktop/svn/gianluca/TSE_2018/evaluation/study4")

demographics = read.csv2("data/AFP - Demographics.csv", sep=",", header = TRUE, quote="\"")

A6 = read.csv2("data/AFP - A6 Responses.csv", sep=",", header = TRUE, quote="\"")

AFP = read.csv2("data/AFP - AFP Responses.csv", sep=",", header = TRUE, quote="\"")

A6$Q1 <- ordered(A6$Q1,
         levels = c("Absolutely not trustable","Not trustable", "Neutral","Trustable","Very trustable"))

A6$Q2 <- ordered(A6$Q2,
                 levels = c("Very unlikely","Unlikely", "Neutral","Likely","Very likely"))

AFP$Q1 <- ordered(AFP$Q1,
                levels = c("Absolutely not trustable","Not trustable", "Neutral","Trustable","Very trustable"))

AFP$Q2 <- ordered(AFP$Q2,
                 levels = c("Very unlikely","Unlikely", "Neutral","Likely","Very likely"))

AFP$Q3 <- ordered(AFP$Q3,
                  levels = c("Absolutely No", "No", "Don't know", "Yes","Absolutely yes"))

AFP$Q4 <- ordered(AFP$Q4,
                  levels = c("Absolutely No", "No", "Don't know", "Yes","Absolutely yes"))

AFP$Q5 <- ordered(AFP$Q5,
                  levels = c("Absolutely No", "No", "Don't know", "Yes","Absolutely yes"))

AFP$Q6 <- ordered(AFP$Q6,
                  levels = c("Absolutely No", "No", "Don't know", "Yes","Absolutely yes"))

AFP$S1 <- ordered(AFP$S1,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

AFP$S2 <- ordered(AFP$S2,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

AFP$S3 <- ordered(AFP$S3,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

AFP$S4 <- ordered(AFP$S4,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

AFP$S5 <- ordered(AFP$S5,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

AFP$S6 <- ordered(AFP$S6,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

AFP$S7 <- ordered(AFP$S7,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

AFP$S8 <- ordered(AFP$S8,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

AFP$S9 <- ordered(AFP$S9,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))

AFP$S10 <- ordered(AFP$S10,
                  levels = c("1 - Strongly disagree", "2 - Disagree", "3 - Neutral", "4 - Agree", "5 - Strongly agree"))


AFP_freq_s1 = round(table(AFP$S1) / 47, digits = 2)
AFP_freq_s2 = round(table(AFP$S2) / 47, digits = 2)
AFP_freq_s3 = round(table(AFP$S3) / 47, digits = 2)
AFP_freq_s4 = round(table(AFP$S4) / 47, digits = 2)
AFP_freq_s5 = round(table(AFP$S5) / 47, digits = 2)
AFP_freq_s6 = round(table(AFP$S6) / 47, digits = 2)
AFP_freq_s7 = round(table(AFP$S7) / 47, digits = 2)
AFP_freq_s8 = round(table(AFP$S8) / 47, digits = 2)
AFP_freq_s9 = round(table(AFP$S9) / 47, digits = 2)
AFP_freq_s10 = round(table(AFP$S10) / 47, digits = 2)

S1_norm = (as.numeric(AFP$S1) -1)
S3_norm = (as.numeric(AFP$S3) -1)
S5_norm = (as.numeric(AFP$S5) -1)
S7_norm = (as.numeric(AFP$S7) -1)
S9_norm = (as.numeric(AFP$S9) -1)

S2_norm = (5 - as.numeric(AFP$S2))
S4_norm = (5 - as.numeric(AFP$S4))
S6_norm = (5 - as.numeric(AFP$S6)) 
S8_norm = (5 - as.numeric(AFP$S8)) 
S10_norm = (5 - as.numeric(AFP$S10))

SUS_score = (sum(S1_norm + S2_norm + S3_norm + S4_norm + S5_norm + 
                   S6_norm + S7_norm + S8_norm + S9_norm + S10_norm) * 2.5)/47

S1n_freq = round(table(factor(S1_norm, levels = 0:4)) / 47, digits = 2)
S2n_freq = round(table(factor(S2_norm, levels = 0:4)) / 47, digits = 2)
S3n_freq = round(table(factor(S3_norm, levels = 0:4)) / 47, digits = 2)
S4n_freq = round(table(factor(S4_norm, levels = 0:4)) / 47, digits = 2)
S5n_freq = round(table(factor(S5_norm, levels = 0:4)) / 47, digits = 2)
S6n_freq = round(table(factor(S6_norm, levels = 0:4)) / 47, digits = 2)
S7n_freq = round(table(factor(S7_norm, levels = 0:4)) / 47, digits = 2)
S8n_freq = round(table(factor(S8_norm, levels = 0:4)) / 47, digits = 2)
S9n_freq = round(table(factor(S9_norm, levels = 0:4)) / 47, digits = 2)
S10n_freq = round(table(factor(S10_norm, levels = 0:4)) / 47, digits = 2)

######################### DATA BY SELF DECLARED ANDROID KNOWLEDGE ###################

joined = inner_join(demographics, AFP, by=c("ID"))
joined_A6 = inner_join(demographics, A6, by=c("ID"))

confidence_1 = subset(joined, joined$AndroidKnowledge == 1)
confidence_2 = subset(joined, joined$AndroidKnowledge == 2)
confidence_3 = subset(joined, joined$AndroidKnowledge == 3)
confidence_4 = subset(joined, joined$AndroidKnowledge == 4)