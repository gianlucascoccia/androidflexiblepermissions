setwd("/Users/gianlucascoccia/Desktop/svn/gianluca/TSE_2018/evaluation/users")
source('load.r')
library(ggplot2)
require(reshape2)
library(dplyr)
library(RColorBrewer)

x_name <- "Android6"
y_name <- "AFP"

################################################ BOXPLOT OF Q1 ################################################ 

df <- data.frame(A6$ID, as.numeric(A6$Q1), as.numeric(AFP$Q1))
colnames(df) <- c("ID", "Android 6", "AFP")
df = melt(df, id.vars = c("ID"))

ggplot(df, aes(variable, value)) +
  geom_boxplot() +
  scale_y_continuous( labels = c("Absolutely  \nnot trustable","Not trustable ","Neutral ","Trustable ","Very    \ntrustable")) +
  scale_x_discrete() +
  ylab(label="Answer") +
  xlab(label = "Permission system") +  
  theme_bw() +
  theme(text = element_text(size=15)) 

################################################ BOXPLOT OF Q2 ################################################ 

df <- data.frame(A6$ID, as.numeric(A6$Q2), as.numeric(AFP$Q2))
colnames(df) <- c("ID", "Android 6", "AFP")
df = melt(df, id.vars = c("ID"))

ggplot(df, aes(variable, value)) +
  geom_boxplot() +
  scale_y_continuous(limits = c(1, 5), labels = c("Very  \nunlikely","Unlikely ","Neutral ","Likely ","Very \nLikely")) +
  scale_x_discrete() +
  ylab(label="Answer") +
  xlab(label = "Permission system") +  
  theme_classic() +
  theme(text = element_text(size=15)) 

############################################ Q1 E Q2 SENZA GGPLOT ###############################################

df <- data.frame(as.numeric(A6$Q1), as.numeric(AFP$Q1), as.numeric(A6$Q2), as.numeric(AFP$Q2))
colnames(df) <- c("Android 6", "AFP", "Android 6", "AFP")

boxplot(df,  yaxt='n', col="grey", drawRect=TRUE, wex=0.5, range=1, xaxt="n")
abline(v=2.5)
par(las=1, pin=c(5,3.5), mar=c(2,5.6,2.5,5.6))
axis(side=2, labels=c("Absolutely \nnot trustable", "Not \ntrustable", "Neutral", "Trustable", "Absolutely \ntrustable "), at=seq(1,5))
axis(side=4, labels=c("Very  \nunlikely","Unlikely ","Neutral ","Likely ","Very \nLikely"), at=seq(1,5))
axis(side=3, labels=c("Android 6", "AFP", "Android 6", "AFP"), at=seq(1,4))
axis(side=1, labels=c(expression('Q'[1]), expression('Q'[2])), at=c(1.5,3.5))

################################################ HISTOGRAM OF Q3 ################################################ 

df <- data.frame(as.numeric(AFP$Q3))
colnames(df) <- c("value")

ggplot(df, aes(x=value)) +
  stat_bin(binwidth = 1, bins = 5) +
  scale_x_continuous(labels = c("Absolutely \nno","No ","Don't know","Yes","Absolutely \nyes")) +
  coord_cartesian(xlim  = c(1, 5)) +
  ylab(label="Count") +
  xlab(label = "Answer") +
  theme_classic() +
  theme(text = element_text(size=15)) 

################################################ HISTOGRAM OF Q4 ################################################ 

df <- data.frame(as.numeric(AFP$Q4))
colnames(df) <- c("value")

ggplot(df, aes(x=value)) +
  stat_bin(binwidth = 1, bins = 5) +
  scale_x_continuous(labels = c("Absolutely \nno","No ","Don't know","Yes","Absolutely \nyes")) +
  coord_cartesian(xlim  = c(1, 5)) +
  ylab(label="Count") +
  xlab(label = "Answer") +
  theme_classic() +
  theme(text = element_text(size=15)) 

################################################ HISTOGRAM OF Q5 ################################################ 

df <- data.frame(as.numeric(AFP$Q5))
colnames(df) <- c("value")

ggplot(df, aes(x=value)) +
  stat_bin(binwidth = 1, bins = 5) +
  scale_x_continuous(labels = c("Absolutely \nno","No ","Don't know","Yes","Absolutely \nyes")) +
  coord_cartesian(xlim  = c(1, 5)) +
  ylab(label="Count") +
  xlab(label = "Answer") +
  theme_classic() +
  theme(text = element_text(size=15)) 

################################################ HISTOGRAM OF Q6 ################################################ 

df <- data.frame(as.numeric(AFP$Q6))
colnames(df) <- c("value")

ggplot(df, aes(x=value)) +
  stat_bin(binwidth = 1, bins = 5) +
  scale_x_continuous(labels = c("Absolutely \nno","No ","Don't know","Yes","Absolutely \nyes")) +
  coord_cartesian(xlim  = c(1, 5)) +
  ylab(label="Count") +
  xlab(label = "Answer") +
  theme_classic() +
  theme(text = element_text(size=15)) 

################################################ BOXPLOT OF Q3 TO Q6 ################################################ 

df <- data.frame(as.numeric(AFP$Q3), as.numeric(AFP$Q4), as.numeric(AFP$Q5), as.numeric(AFP$Q6))
colnames(df) <- c("Q3", "Q4", "Q5", "Q6")

boxplot(df,  yaxt='n', col="grey", drawRect=TRUE, wex=0.5, range=1, ylim=c(1,5), xaxt="n")
abline(v=c(1.5, 2.5, 3.5))
par(las=1, pin=c(5,3.5), mar=c(2,5.6,0.5,2))
axis(side=2, labels=c("Absolutely \nno","No ","Don't know","Yes","Absolutely \nyes"), at=seq(1,5))
axis(side=1, labels=c(expression('Q'[3]), expression('Q'[4]), expression('Q'[5]), expression('Q'[6])), at=c(1,2,3,4))

################################################ HEATMAP OF POSITIVE STATEMENTS  ############################################

positive_statements <- rbind(AFP_freq_s1, AFP_freq_s3, AFP_freq_s5, AFP_freq_s7, AFP_freq_s9)
positive_statements <- melt(t(positive_statements) * 100)
colnames(positive_statements) = c('Score', 'rowID', 'Percentage')
hm.palette <- colorRampPalette(brewer.pal(9, 'Oranges'), space='Lab')

addline_format <- function(x,...){
  gsub('\\s','\n',x)
}

ggplot(positive_statements, aes(x = Score, y = rowID, fill = Percentage)) + 
  geom_tile() + 
  #coord_equal() +
  scale_fill_gradientn(colours = hm.palette(100)) +
  scale_y_discrete("Statement", labels = c("S1","S3","S5","S7","S9")) +
  scale_x_discrete(labels = addline_format(c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))) +
  xlab("Score") + 
  theme(axis.text=element_text(size=12), axis.title=element_text(size=12)) +
  theme(axis.text.x = element_text(angle = 70, hjust = 1, colour = c('red1', 'red4', 'black', 'green4', 'green2')))

################################################ HEATMAP OF NEGATIVE STATEMENTS  ############################################

negative_statements <- rbind(AFP_freq_s2, AFP_freq_s4, AFP_freq_s6, AFP_freq_s8, AFP_freq_s10)
negative_statements <- melt(t(negative_statements) * 100)
colnames(negative_statements) = c('Score', 'rowID', 'Percentage')
hm.palette <- colorRampPalette(brewer.pal(9, 'Oranges'), space='Lab')

ggplot(negative_statements, aes(x = Score, y = rowID, fill = Percentage)) + 
  geom_tile() + 
  #coord_equal() +
  scale_fill_gradientn(colours = hm.palette(100)) +
  scale_y_discrete("Statement", labels = c("S2","S4","S6","S8","S10")) +
  scale_x_discrete(labels = addline_format(c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))) +
  xlab("Score") + 
  theme(axis.text=element_text(size=12), axis.title=element_text(size=12)) +
  theme(axis.text.x = element_text(angle = 70, hjust = 1, 
                                   colour = c('red1', 'red4', 'black', 'green4', 'green2')))


################################## COMPLETE NORMALIZED STATEMENTS HEATMAP  ############################################

norm_statements = rbind(S1n_freq, S2n_freq, S3n_freq, S4n_freq, S5n_freq, S6n_freq, S7n_freq, S8n_freq, S9n_freq, 
                        S10n_freq)
norm_statements <- melt(norm_statements )
colnames(norm_statements) = c('Score', 'rowID', 'Percentage')
hm.palette <- colorRampPalette(brewer.pal(9, 'Oranges'), space='Lab')

ggplot(norm_statements, aes(x = Score, y = rowID, fill = Percentage)) + 
  geom_tile() + 
  #coord_equal() +
  scale_fill_gradientn(colours = hm.palette(100)) +
  scale_y_continuous() +
  scale_x_discrete(labels = c(expression('S'[1]),expression('S'[2]),expression('S'[3]),expression('S'[4]),
                              expression('S'[5]),expression('S'[6]),expression('S'[7]),expression('S'[8]),
                              expression('S'[9]),expression('S'[10]))) +
  xlab("Statement") + 
  ylab("Score") +
  theme(axis.text=element_text(size=14), axis.title=element_text(size=14)) +
  theme(axis.text.y = element_text(colour = c('red1', 'red4', 'black', 'green4', 'green2'))) +
  theme(legend.position="bottom")


############################# WILCOX Q1 Q2 #######################################

wilcox.test(as.numeric(AFP$Q1), as.numeric(A6$Q1), alternative = 'two.sided')

wilcox.test(as.numeric(AFP$Q2), as.numeric(A6$Q2), alternative = 'two.sided')

