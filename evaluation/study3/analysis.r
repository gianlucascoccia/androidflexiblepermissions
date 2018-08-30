setwd("/Users/gianlucascoccia/Desktop/svn/gianluca/TSE_2018/evaluation/developers")
source("load.r")

library(ggplot2)
require(reshape2)
library(dplyr)
library(RColorBrewer)

##################################  AVERAGES  ############################################

mean(questions$experience)
sd(questions$experience)
getmode(questions$appsDeveloped)
getmode(questions$membersCount)


mean(times)
sd(times)

################################## BOXPLOTS Q1 Q2 Q3  ############################################

df <- data.frame(as.numeric(questions$Q1), as.numeric(questions$Q2), as.numeric(questions$Q3))
colnames(df) <- c("Q1", "Q2", "Q3")

boxplot(df,  yaxt='n', col="grey", drawRect=TRUE, wex=0.5, range=1, ylim=c(1,5), xaxt="n")
abline(v=c(1.5, 2.5))
par(las=1, pin=c(5,3.5), mar=c(2,5.6,0.5,2))
axis(side=2, labels=c("Absolutely\n no","No", "Don't know","Yes","Absolutely\n yes"), at=seq(1,5))
axis(side=1, labels=c(expression('q'[1]), expression('q'[2]), expression('q'[3])), at=seq(1,3))

################################## BOXPLOTS STATEMENTS  ############################################

df <- data.frame(as.numeric(questions$S1), as.numeric(questions$S2), as.numeric(questions$S3), as.numeric(questions$S4), as.numeric(questions$S5), as.numeric(questions$S6), as.numeric(questions$S7), as.numeric(questions$S8), as.numeric(questions$S9), as.numeric(questions$S10))
colnames(df) <- c("S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8", "S9", "S10")

boxplot(df,  yaxt='n', col="grey", drawRect=TRUE, wex=0.5, range=1, ylim=c(1,5), xaxt="n")
abline(v=c(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5))
par(las=1, pin=c(5,3.5), mar=c(2,5.6,0.5,2))
axis(side=2, labels=c(0,1,2,3,4), at=seq(1,5))
axis(side=1, labels=c(expression('s'[1]), expression('s'[2]), expression('s'[3]), expression('s'[4]), expression('s'[5]), expression('s'[6]), expression('s'[7]), expression('s'[8]), expression('s'[9]), expression('s'[10])), at=seq(1,10))

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
  scale_x_discrete(labels = c(expression('s'[1]),expression('s'[2]),expression('s'[3]),expression('s'[4]),
                              expression('s'[5]),expression('s'[6]),expression('s'[7]),expression('s'[8]),
                              expression('s'[9]),expression('s'[10]))) +
  xlab("Statement") + 
  ylab("Score") +
  theme(axis.text=element_text(size=14), axis.title=element_text(size=14)) +
  theme(axis.text.y = element_text(colour = c('red1', 'red4', 'black', 'green4', 'green2'))) +
  theme(legend.position="bottom")
