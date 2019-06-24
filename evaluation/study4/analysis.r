setwd("/Users/gianlucascoccia/Desktop/svn2/gianluca/TSE_2018/evaluation/study4")
source('load.r')
library(ggplot2)
require(reshape2)
library(dplyr)
library(RColorBrewer)
require(lattice)
require(latticeExtra)
require(HH)
library(likert)
library(cowplot)
library(lessR)

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

############################################ STACKED BAR PLOT Q1 ###############################################

xlabs = c(expression('Android 6'), expression('AFP'))
ylabs = c("Absolutely\n yes","Yes", "Don't know","No","Absolutely\n no")

df <- data.frame(as.numeric(A6$Q1), as.numeric(AFP$Q1), as.numeric(A6$Q2), as.numeric(AFP$Q2))
colnames(df) <- c("Android 6", "AFP", "Android 6", "AFP")

df.likert = data.frame("Android 6" = c(sum(df[,1] == 1), sum(df[,1] == 2), sum(df[,1] == 3), sum(df[,1] == 4), sum(df[,1] == 5)), 
                       "AFP" = c(sum(df[,2] == 1), sum(df[,2] == 2), sum(df[,2] == 3), sum(df[,2] == 4), sum(df[,2] == 5)))
df.likert = t(df.likert)
colnames(df.likert) = rev(ylabs)

plot.likert(df.likert, horizontal=FALSE,
            main="",
            rightAxis  = TRUE,
            aspect = 0.95,
            xlab = "Answers count",
            ylab = "Question",
            scales=list(x=list(at=seq(1,3), labels=xlabs, cex=1.2), y=list(at=c(-20,-15, -10, -5, 0, 5, 10, 15, 20, 25, 30, 35, 40, 45), cex=1.2)),
            panel = panel.barchart,
            auto.key=list(points=FALSE, 
                          space="right", 
                          columns=1, 
                          reverse=TRUE, 
                          padding.text=8,
                          cex=1.025),
            par.settings.in=list(lattice.options(
              layout.heights=list(bottom.padding=list(x=0), top.padding=list(x=0)),
              layout.widths=list(left.padding=list(x=0), right.padding=list(x=0))
            )))
############################################ GGPLOT LIKERT ############################################

df_ggplot = df[,c(1,2)]
df_ggplot$`Android 6` = as.factor(df_ggplot$`Android 6`)
df_ggplot$AFP = as.factor(df_ggplot$AFP)
mylevels = c("1","2","3","4","5")
levels(df_ggplot$AFP) = mylevels
levels(df_ggplot$`Android 6`) = mylevels

HH::likert(likert::likert(df_ggplot),
           horizontal=FALSE,
           main="",
           aspect = 0.95,
           ylab = "Question")


############################################ STACKED BAR PLOT Q2 ###############################################

xlabs = c(expression('Android 6'), expression('AFP'))
ylabs = c("Very \nlikely","Likely ","Neutral ","Unlikely ","Very \nunlikely")

df <- data.frame(as.numeric(A6$Q1), as.numeric(AFP$Q1), as.numeric(A6$Q2), as.numeric(AFP$Q2))
colnames(df) <- c("Android 6", "AFP", "Android 6", "AFP")

df.likert = data.frame("Android 6" = c(sum(df[,3] == 1), sum(df[,3] == 2), sum(df[,3] == 3), sum(df[,3] == 4), sum(df[,3] == 5)),
                       "AFP" = c(sum(df[,4] == 1), sum(df[,4] == 2), sum(df[,4] == 3), sum(df[,4] == 4), sum(df[,4] == 5)))
df.likert = t(df.likert)
colnames(df.likert) = rev(ylabs)

plot.likert(df.likert, horizontal=FALSE,
            main="",
            aspect = 0.95,
            xlab = "Answers count",
            ylab = "Question",
            scales=list(x=list(at=seq(1,3), labels=xlabs, cex=1.2), y=list(at=c(-20,-15, -10, -5, 0, 5, 10, 15, 20, 25, 30, 35, 40, 45), cex=1.2)),
            auto.key=list(points=FALSE, 
                          space="right", 
                          columns=1, 
                          reverse=TRUE, 
                          padding.text=8,
                          cex=1.025),
            par.settings.in=list(lattice.options(
              layout.heights=list(bottom.padding=list(x=0), top.padding=list(x=0)),
              layout.widths=list(left.padding=list(x=0), right.padding=list(x=0))
            )))

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

################################################ BOXPLOT AND STACKED PLOT OF Q3 TO Q6 ################################################ 

xlabs = c(expression('Q'[3]), expression('Q'[4]), expression('Q'[5]), expression('Q'[6]))
ylabs = c("Absolutely\n yes","Yes", "Don't know","No","Absolutely\n no")

df <- data.frame(as.numeric(AFP$Q3), as.numeric(AFP$Q4), as.numeric(AFP$Q5), as.numeric(AFP$Q6))
colnames(df) <- c("Q3", "Q4", "Q5", "Q6")

df.likert = data.frame("Q3" = c(sum(df$Q3 == 1), sum(df$Q3 == 2), sum(df$Q3 == 3), sum(df$Q3 == 4), sum(df$Q3 == 5)), 
                       "Q4" = c(sum(df$Q4 == 1), sum(df$Q4 == 2), sum(df$Q4 == 3), sum(df$Q4 == 4), sum(df$Q4 == 5)),
                       "Q5" = c(sum(df$Q5 == 1), sum(df$Q5 == 2), sum(df$Q5 == 3), sum(df$Q5 == 4), sum(df$Q5 == 5)),
                       "Q6" = c(sum(df$Q6 == 1), sum(df$Q6 == 2), sum(df$Q6 == 3), sum(df$Q6 == 4), sum(df$Q6 == 5)))
df.likert = t(df.likert)
colnames(df.likert) = rev(ylabs)
  
boxplot(df,  yaxt='n', col="grey", drawRect=TRUE, wex=0.5, range=1, ylim=c(1,5), xaxt="n")
abline(v=c(1.5, 2.5, 3.5))
par(las=1, pin=c(5,3.5), mar=c(2,5.6,0.5,2))
axis(side=2, labels=c("Absolutely \nno","No ","Don't know","Yes","Absolutely \nyes"), at=seq(1,5))
axis(side=1, labels=c(expression('Q'[3]), expression('Q'[4]), expression('Q'[5]), expression('Q'[6])), at=c(1,2,3,4))

plot.likert(df.likert, horizontal=FALSE,
            main="",
            aspect = 0.95,
            xlab = "Answers count",
            ylab = "Question",
            scales=list(x=list(at=seq(1,4), labels=xlabs, cex=1.2), y=list(at=c(-5,0,5,10,15,20,25,30,35,40,45,50), cex=1.2)),
            auto.key=list(points=FALSE, 
                          space="right", 
                          columns=1, 
                          reverse=TRUE, 
                          padding.text=8,
                          cex=1.025),
            par.settings.in=list(lattice.options(
              layout.heights=list(bottom.padding=list(x=0), top.padding=list(x=0)),
              layout.widths=list(left.padding=list(x=0), right.padding=list(x=0))
            )))

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

######################### CIRCLE PLOT CONFIG ###################

one_size = 0.301
two_size = 0.34
three_size = 0.37

######################### Q1 BY SELF DECLARED ANDROID KNOWLEDGE ###################

A6_Q1_by_know = table(joined$AndroidKnowledge, joined_A6$Q1)
AFP_Q1_by_know = table(joined$AndroidKnowledge, joined$Q1)
rownames(AFP_Q1_by_know) = c("6", "7", "8", "9", "10")
Q1_by_know = Merge(A6_Q1_by_know, AFP_Q1_by_know)
Q1_by_know = melt(Q1_by_know)
colnames(Q1_by_know) = c("Knowledge", "Answer", "Count")
Q1_by_know$radious = Q1_by_know$Count/10

# Increase values too small to have better readability
Q1_by_know$radious[Q1_by_know$radious == 0.1] = one_size
Q1_by_know$radious[Q1_by_know$radious == 0.2] = two_size
Q1_by_know$radious[Q1_by_know$radious == 0.3] = three_size

fileName = "Q1_by_knowledge.png"
XLabel = "Android                                          AFP"
YLabel = "Answer"

p <- ggplot(Q1_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  geom_vline(xintercept=5.5) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  scale_x_continuous(breaks=seq(1,10), labels = c("1", "2", "3", "4", "5", "1", "2", "3", "4", "5")) +
  scale_y_discrete(labels = c("Absolutely   \n not trustable", "Not trustable", "Neutral", "Trustable", "Very trustable")) +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  labs(caption="Android knowledge") + 
  theme(plot.caption = element_text(hjust=0.5, size=rel(1)))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### Q2 BY SELF DECLARED ANDROID KNOWLEDGE ###################

A6_Q2_by_know = table(joined$AndroidKnowledge, joined_A6$Q2)
AFP_Q2_by_know = table(joined$AndroidKnowledge, joined$Q2)
rownames(AFP_Q2_by_know) = c("6", "7", "8", "9", "10")
Q2_by_know = Merge(A6_Q2_by_know, AFP_Q2_by_know)
Q2_by_know = melt(Q2_by_know)
colnames(Q2_by_know) = c("Knowledge", "Answer", "Count")
Q2_by_know$radious = Q2_by_know$Count/10

# Increase values too small to have better readability
Q2_by_know$radious[Q2_by_know$radious == 0.1] = one_size
Q2_by_know$radious[Q2_by_know$radious == 0.2] = two_size
Q2_by_know$radious[Q2_by_know$radious == 0.3] = three_size

fileName = "Q2_by_knowledge.png"
XLabel = "Android                                          AFP"
YLabel = "Answer"

p <- ggplot(Q2_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  geom_vline(xintercept=5.5) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  scale_x_continuous(breaks=seq(1,10), labels = c("1", "2", "3", "4", "5", "1", "2", "3", "4", "5")) +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  labs(caption="Android knowledge") + 
  theme(plot.caption = element_text(hjust=0.5, size=rel(1)))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### Q3 BY SELF DECLARED ANDROID KNOWLEDGE ###################

Q3_by_know = table(joined$AndroidKnowledge, joined$Q3)
Q3_by_know = melt(Q3_by_know)
colnames(Q3_by_know) = c("Knowledge", "Answer", "Count")
Q3_by_know$radious = Q3_by_know$Count/10

# Increase values too small to have better readability
Q3_by_know$radious[Q3_by_know$radious == 0.1] = one_size
Q3_by_know$radious[Q3_by_know$radious == 0.2] = two_size
Q3_by_know$radious[Q3_by_know$radious == 0.3] = three_size

fileName = "Q3_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(Q3_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel)

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### Q4 BY SELF DECLARED ANDROID KNOWLEDGE ###################

Q4_by_know = table(joined$AndroidKnowledge, joined$Q4)
Q4_by_know = melt(Q4_by_know)
colnames(Q4_by_know) = c("Knowledge", "Answer", "Count")
Q4_by_know$radious = Q4_by_know$Count/10

# Increase values too small to have better readability
Q4_by_know$radious[Q4_by_know$radious == 0.1] = one_size
Q4_by_know$radious[Q4_by_know$radious == 0.2] = two_size
Q4_by_know$radious[Q4_by_know$radious == 0.3] = three_size

fileName = "Q4_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(Q4_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel)

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### Q5 BY SELF DECLARED ANDROID KNOWLEDGE ###################

Q5_by_know = table(joined$AndroidKnowledge, joined$Q5)
Q5_by_know = melt(Q5_by_know)
colnames(Q5_by_know) = c("Knowledge", "Answer", "Count")
Q5_by_know$radious = Q5_by_know$Count/10

# Increase values too small to have better readability
Q5_by_know$radious[Q5_by_know$radious == 0.1] = one_size
Q5_by_know$radious[Q5_by_know$radious == 0.2] = two_size
Q5_by_know$radious[Q5_by_know$radious == 0.3] = three_size

fileName = "Q5_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(Q5_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel)

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### Q6 BY SELF DECLARED ANDROID KNOWLEDGE ###################

Q6_by_know = table(joined$AndroidKnowledge, joined$Q6)
Q6_by_know = melt(Q6_by_know)
colnames(Q6_by_know) = c("Knowledge", "Answer", "Count")
Q6_by_know$radious = Q6_by_know$Count/10

# Increase values too small to have better readability
Q6_by_know$radious[Q6_by_know$radious == 0.1] = one_size
Q6_by_know$radious[Q6_by_know$radious == 0.2] = two_size
Q6_by_know$radious[Q6_by_know$radious == 0.3] = three_size

fileName = "Q6_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(Q6_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel)

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### S1 BY SELF DECLARED ANDROID KNOWLEDGE ###################

S1_by_know = table(joined$AndroidKnowledge, joined$S1)
S1_by_know = melt(S1_by_know)
colnames(S1_by_know) = c("Knowledge", "Answer", "Count")
S1_by_know$radious = S1_by_know$Count/10

# Increase values too small to have better readability
S1_by_know$radious[S1_by_know$radious == 0.1] = one_size 
S1_by_know$radious[S1_by_know$radious == 0.2] = two_size 
S1_by_know$radious[S1_by_know$radious == 0.3] = three_size 

fileName = "S1_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(S1_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  scale_y_discrete(labels=c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### S2 BY SELF DECLARED ANDROID KNOWLEDGE ###################

S2_by_know = table(joined$AndroidKnowledge, joined$S2)
S2_by_know = melt(S2_by_know)
colnames(S2_by_know) = c("Knowledge", "Answer", "Count")
S2_by_know$radious = S2_by_know$Count/10

# Increase values too small to have better readability
S2_by_know$radious[S2_by_know$radious == 0.1] = one_size 
S2_by_know$radious[S2_by_know$radious == 0.2] = two_size 
S2_by_know$radious[S2_by_know$radious == 0.3] = three_size 

fileName = "S2_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(S2_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#7589e9", "#a1abe7", "#e2e2e2", "#e999a9", "#e36985")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  scale_y_discrete(labels=c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### S3 BY SELF DECLARED ANDROID KNOWLEDGE ###################

S3_by_know = table(joined$AndroidKnowledge, joined$S3)
S3_by_know = melt(S3_by_know)
colnames(S3_by_know) = c("Knowledge", "Answer", "Count")
S3_by_know$radious = S3_by_know$Count/10

# Increase values too small to have better readability
S3_by_know$radious[S3_by_know$radious == 0.1] = one_size 
S3_by_know$radious[S3_by_know$radious == 0.2] = two_size 
S3_by_know$radious[S3_by_know$radious == 0.3] = three_size 

fileName = "S3_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(S3_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  scale_y_discrete(labels=c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### S4 BY SELF DECLARED ANDROID KNOWLEDGE ###################

S4_by_know = table(joined$AndroidKnowledge, joined$S4)
S4_by_know = melt(S4_by_know)
colnames(S4_by_know) = c("Knowledge", "Answer", "Count")
S4_by_know$radious = S4_by_know$Count/10

# Increase values too small to have better readability
S4_by_know$radious[S4_by_know$radious == 0.1] = one_size 
S4_by_know$radious[S4_by_know$radious == 0.2] = two_size 
S4_by_know$radious[S4_by_know$radious == 0.3] = three_size 

fileName = "S4_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(S4_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#7589e9", "#a1abe7", "#e2e2e2", "#e999a9", "#e36985")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  scale_y_discrete(labels=c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)


######################### S5 BY SELF DECLARED ANDROID KNOWLEDGE ###################

S5_by_know = table(joined$AndroidKnowledge, joined$S5)
S5_by_know = melt(S5_by_know)
colnames(S5_by_know) = c("Knowledge", "Answer", "Count")
S5_by_know$radious = S5_by_know$Count/10

# Increase values too small to have better readability
S5_by_know$radious[S5_by_know$radious == 0.1] = one_size 
S5_by_know$radious[S5_by_know$radious == 0.2] = two_size 
S5_by_know$radious[S5_by_know$radious == 0.3] = three_size 

fileName = "S5_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(S5_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  scale_y_discrete(labels=c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### S6 BY SELF DECLARED ANDROID KNOWLEDGE ###################

S6_by_know = table(joined$AndroidKnowledge, joined$S6)
S6_by_know = melt(S6_by_know)
colnames(S6_by_know) = c("Knowledge", "Answer", "Count")
S6_by_know$radious = S6_by_know$Count/10

# Increase values too small to have better readability
S6_by_know$radious[S6_by_know$radious == 0.1] = one_size 
S6_by_know$radious[S6_by_know$radious == 0.2] = two_size 
S6_by_know$radious[S6_by_know$radious == 0.3] = three_size 

fileName = "S6_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(S6_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#7589e9", "#a1abe7", "#e2e2e2", "#e999a9", "#e36985")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  scale_y_discrete(labels=c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### S7 BY SELF DECLARED ANDROID KNOWLEDGE ###################

S7_by_know = table(joined$AndroidKnowledge, joined$S7)
S7_by_know = melt(S7_by_know)
colnames(S7_by_know) = c("Knowledge", "Answer", "Count")
S7_by_know$radious = S7_by_know$Count/10

# Increase values too small to have better readability
S7_by_know$radious[S7_by_know$radious == 0.1] = one_size 
S7_by_know$radious[S7_by_know$radious == 0.2] = two_size 
S7_by_know$radious[S7_by_know$radious == 0.3] = three_size 

fileName = "S7_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(S7_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  scale_y_discrete(labels=c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### S8 BY SELF DECLARED ANDROID KNOWLEDGE ###################

S8_by_know = table(joined$AndroidKnowledge, joined$S8)
S8_by_know = melt(S8_by_know)
colnames(S8_by_know) = c("Knowledge", "Answer", "Count")
S8_by_know$radious = S8_by_know$Count/10

# Increase values too small to have better readability
S8_by_know$radious[S8_by_know$radious == 0.1] = one_size 
S8_by_know$radious[S8_by_know$radious == 0.2] = two_size 
S8_by_know$radious[S8_by_know$radious == 0.3] = three_size 

fileName = "S8_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(S8_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#7589e9", "#a1abe7", "#e2e2e2", "#e999a9", "#e36985")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  scale_y_discrete(labels=c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### S9 BY SELF DECLARED ANDROID KNOWLEDGE ###################

S9_by_know = table(joined$AndroidKnowledge, joined$S9)
S9_by_know = melt(S9_by_know)
colnames(S9_by_know) = c("Knowledge", "Answer", "Count")
S9_by_know$radious = S9_by_know$Count/10

# Increase values too small to have better readability
S9_by_know$radious[S9_by_know$radious == 0.1] = one_size 
S9_by_know$radious[S9_by_know$radious == 0.2] = two_size 
S9_by_know$radious[S9_by_know$radious == 0.3] = three_size 

fileName = "S9_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(S9_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  scale_y_discrete(labels=c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### S10 BY SELF DECLARED ANDROID KNOWLEDGE ###################

S10_by_know = table(joined$AndroidKnowledge, joined$S10)
S10_by_know = melt(S10_by_know)
colnames(S10_by_know) = c("Knowledge", "Answer", "Count")
S10_by_know$radious = S10_by_know$Count/10

# Increase values too small to have better readability
S10_by_know$radious[S10_by_know$radious == 0.1] = one_size 
S10_by_know$radious[S10_by_know$radious == 0.2] = two_size 
S10_by_know$radious[S10_by_know$radious == 0.3] = three_size 

fileName = "S10_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"

p <- ggplot(S10_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*18)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=5, alpha=1) +
  scale_fill_manual(values=c("#7589e9", "#a1abe7", "#e2e2e2", "#e999a9", "#e36985")) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=15, hjust=1, vjust=0), axis.text.y = element_text(size=15),
        axis.title.x = element_text(size=15), axis.title.y = element_text(size=15)) + 
  theme(axis.line = element_line(color = 'black')) +
  labs(x=XLabel, y=YLabel) +
  scale_y_discrete(labels=c("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree"))

print(p)
ggsave(fileName, p, width=2.5, height=1.5, units="in", scale=3)

######################### Q3 TO Q6 BY SELF DECLARED ANDROID KNOWLEDGE ###################

Q3_by_know = table(joined$AndroidKnowledge, joined$Q3)
Q4_by_know = table(joined$AndroidKnowledge, joined$Q4)
rownames(Q4_by_know) = c("6", "7", "8", "9", "10")
QS_by_know = Merge(Q3_by_know, Q4_by_know)
Q5_by_know = table(joined$AndroidKnowledge, joined$Q5)
rownames(Q5_by_know) = c("11", "12", "13", "14", "15")
QS_by_know = Merge(QS_by_know, Q5_by_know)
Q6_by_know = table(joined$AndroidKnowledge, joined$Q6)
rownames(Q6_by_know) = c("16", "17", "18", "19", "20")
QS_by_know = Merge(QS_by_know, Q6_by_know)
QS_by_know = melt(QS_by_know)
colnames(QS_by_know) = c("Knowledge", "Answer", "Count")
QS_by_know$radious = QS_by_know$Count/10

# Increase values too small to have better readability
QS_by_know$radious[QS_by_know$radious == 0.1] = one_size
QS_by_know$radious[QS_by_know$radious == 0.2] = two_size
QS_by_know$radious[QS_by_know$radious == 0.3] = three_size

fileName = "Q3Q4Q5Q6_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"
x_labs = c("1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5")
subgraph_labels = c(expression('Q'[3]), expression('Q'[4]), expression('Q'[5]), expression('Q'[6]))

p <- ggplot(QS_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*21)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=6.5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  geom_vline(xintercept=5.5) +
  geom_vline(xintercept=10.5) +
  geom_vline(xintercept=15.5) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  scale_x_continuous(breaks = seq(1,20), labels = x_labs, sec.axis = dup_axis(~./5+0.36, breaks=seq(1,4), labels=subgraph_labels, name = "")) +
  scale_y_discrete(labels=c("Absolutely \nno", "No", "Don't\n know", "Yes", "Absolutely\n yes")) +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=20, hjust=1, vjust=0), axis.text.y = element_text(size=20),
        axis.title.x = element_text(size=20), axis.title.y = element_text(size=20),
        axis.line = element_line(color = 'black'), axis.line.x.top = element_blank(), 
        axis.ticks.x.top = element_blank()) +
  labs(x=XLabel, y=YLabel) 

print(p)
ggsave(fileName, p, width=5.5, height=1.8, units="in", scale=3)

######################### ODD S# GROUPED #######################

S1_by_know = table(joined$AndroidKnowledge, joined$S1)
S3_by_know = table(joined$AndroidKnowledge, joined$S3)
rownames(S3_by_know) = c("6", "7", "8", "9", "10")
odd_sus_by_know = Merge(S1_by_know, S3_by_know)
S5_by_know = table(joined$AndroidKnowledge, joined$S5)
rownames(S5_by_know) = c("11", "12", "13", "14", "15")
odd_sus_by_know = Merge(odd_sus_by_know, S5_by_know)
S7_by_know = table(joined$AndroidKnowledge, joined$S7)
rownames(S7_by_know) = c("16", "17", "18", "19", "20")
odd_sus_by_know = Merge(odd_sus_by_know, S7_by_know)
S9_by_know = table(joined$AndroidKnowledge, joined$S9)
rownames(S9_by_know) = c("21", "22", "23", "24", "25")
odd_sus_by_know = Merge(odd_sus_by_know, S9_by_know)
odd_sus_by_know = melt(odd_sus_by_know)
colnames(odd_sus_by_know) = c("Knowledge", "Answer", "Count")
odd_sus_by_know$radious = odd_sus_by_know$Count/10

# Increase values too small to have better readability
odd_sus_by_know$radious[odd_sus_by_know$radious == 0.1] = one_size
odd_sus_by_know$radious[odd_sus_by_know$radious == 0.2] = two_size
odd_sus_by_know$radious[odd_sus_by_know$radious == 0.3] = three_size

fileName = "odd_sus_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"
x_labs = c("1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5")
subgraph_labels = c(expression('S'[1]), expression('S'[3]), expression('S'[5]), expression('S'[7]), expression('S'[9]))

p <- ggplot(odd_sus_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*21)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=6.5, alpha=1) +
  scale_fill_manual(values=c("#e36985", "#e999a9", "#e2e2e2", "#a1abe7", "#7589e9")) +
  geom_vline(xintercept=5.5) +
  geom_vline(xintercept=10.5) +
  geom_vline(xintercept=15.5) +
  geom_vline(xintercept=20.5) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  scale_x_continuous(breaks = seq(1,25), labels = x_labs, sec.axis = dup_axis(~./5+0.36, breaks=seq(1,5), labels=subgraph_labels, name = "")) +
  scale_y_discrete(labels=c("Strongly \ndisagree", "Disagree", "Neutral", "Agree", "    Strongly\n agree")) +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=20, hjust=1, vjust=0), axis.text.y = element_text(size=20),
        axis.title.x = element_text(size=20), axis.title.y = element_text(size=20),
        axis.line = element_line(color = 'black'), axis.line.x.top = element_blank(), 
        axis.ticks.x.top = element_blank()) +
  labs(x=XLabel, y=YLabel) 

print(p)
ggsave(fileName, p, width=6.5, height=1.5, units="in", scale=3)

######################### EVEN S# GROUPED #######################

S2_by_know = table(joined$AndroidKnowledge, joined$S2)
S4_by_know = table(joined$AndroidKnowledge, joined$S4)
rownames(S4_by_know) = c("6", "7", "8", "9", "10")
odd_sus_by_know = Merge(S2_by_know, S4_by_know)
S6_by_know = table(joined$AndroidKnowledge, joined$S6)
rownames(S6_by_know) = c("11", "12", "13", "14", "15")
odd_sus_by_know = Merge(odd_sus_by_know, S6_by_know)
S8_by_know = table(joined$AndroidKnowledge, joined$S8)
rownames(S8_by_know) = c("16", "17", "18", "19", "20")
odd_sus_by_know = Merge(odd_sus_by_know, S8_by_know)
S10_by_know = table(joined$AndroidKnowledge, joined$S10)
rownames(S10_by_know) = c("21", "22", "23", "24", "25")
odd_sus_by_know = Merge(odd_sus_by_know, S10_by_know)
odd_sus_by_know = melt(odd_sus_by_know)
colnames(odd_sus_by_know) = c("Knowledge", "Answer", "Count")
odd_sus_by_know$radious = odd_sus_by_know$Count/10

# Increase values too small to have better readability
odd_sus_by_know$radious[odd_sus_by_know$radious == 0.1] = one_size
odd_sus_by_know$radious[odd_sus_by_know$radious == 0.2] = two_size
odd_sus_by_know$radious[odd_sus_by_know$radious == 0.3] = three_size

fileName = "even_sus_by_knowledge.png"
XLabel = "Android knowledge"
YLabel = "Answer"
x_labs = c("1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5")
subgraph_labels = c(expression('S'[2]), expression('S'[4]), expression('S'[6]), expression('S'[8]), expression('S'[10]))

p <- ggplot(odd_sus_by_know, aes(Knowledge, Answer)) + 
  geom_point(aes(fill=Answer, size=ifelse(radious==0, NA, radious*21)),shape=21,color="black") +
  geom_text(aes(label=ifelse(Count==0, NA, Count)),size=6.5, alpha=1) +
  scale_fill_manual(values=c("#7589e9", "#a1abe7", "#e2e2e2", "#e999a9", "#e36985")) +
  geom_vline(xintercept=5.5) +
  geom_vline(xintercept=10.5) +
  geom_vline(xintercept=15.5) +
  geom_vline(xintercept=20.5) +
  guides(fill=FALSE, size=FALSE) +
  scale_size_identity() +
  scale_x_continuous(breaks = seq(1,25), labels = x_labs, sec.axis = dup_axis(~./5+0.36, breaks=seq(1,5), labels=subgraph_labels, name = "")) +
  scale_y_discrete(labels=c("Strongly \ndisagree", "Disagree", "Neutral", "Agree", "    Strongly\n agree")) +
  theme_cowplot() +
  theme(panel.grid.major = element_line(colour = "grey")) +
  theme(axis.text.x = element_text(size=20, hjust=1, vjust=0), axis.text.y = element_text(size=20),
        axis.title.x = element_text(size=20), axis.title.y = element_text(size=20),
        axis.line = element_line(color = 'black'), axis.line.x.top = element_blank(), 
        axis.ticks.x.top = element_blank()) +
  labs(x=XLabel, y=YLabel) 

print(p)
ggsave(fileName, p, width=6.5, height=1.5, units="in", scale=3)


# TODO
######################### SPLIT OF SUS BY ANDROID KNOWLEDGE #######################

one_know = subset(joined, joined$AndroidKnowledge == 1)
two_know = subset(joined, joined$AndroidKnowledge == 2)
three_know = subset(joined, joined$AndroidKnowledge == 3)
four_know = subset(joined, joined$AndroidKnowledge == 4)
five_know = subset(joined, joined$AndroidKnowledge == 5) 

one_S1_norm = (as.numeric(one_know$S1) -1)
one_S3_norm = (as.numeric(one_know$S3) -1)
one_S5_norm = (as.numeric(one_know$S5) -1)
one_S7_norm = (as.numeric(one_know$S7) -1)
one_S9_norm = (as.numeric(one_know$S9) -1)

one_S2_norm = (5 - as.numeric(one_know$S2))
one_S4_norm = (5 - as.numeric(one_know$S4))
one_S6_norm = (5 - as.numeric(one_know$S6)) 
one_S8_norm = (5 - as.numeric(one_know$S8)) 
one_S10_norm = (5 - as.numeric(one_know$S10))

one_SUS_score = (sum(one_S1_norm + one_S2_norm + one_S3_norm + one_S4_norm + one_S5_norm + 
                       one_S6_norm + one_S7_norm + one_S8_norm + one_S9_norm + one_S10_norm) 
                 * 2.5)/nrow(one_know)

two_S1_norm = (as.numeric(two_know$S1) -1)
two_S3_norm = (as.numeric(two_know$S3) -1)
two_S5_norm = (as.numeric(two_know$S5) -1)
two_S7_norm = (as.numeric(two_know$S7) -1)
two_S9_norm = (as.numeric(two_know$S9) -1)

two_S2_norm = (5 - as.numeric(two_know$S2))
two_S4_norm = (5 - as.numeric(two_know$S4))
two_S6_norm = (5 - as.numeric(two_know$S6)) 
two_S8_norm = (5 - as.numeric(two_know$S8)) 
two_S10_norm = (5 - as.numeric(two_know$S10))

two_SUS_score = (sum(two_S1_norm + two_S2_norm + two_S3_norm + two_S4_norm + two_S5_norm + 
                       two_S6_norm + two_S7_norm + two_S8_norm + two_S9_norm + two_S10_norm) 
                 * 2.5)/nrow(two_know)


######################### ANSWERS BY GENDER #######################

table(joined_A6$Q1, joined_A6$Gender)
table(joined$Q1, joined$Gender)

table(joined_A6$Q2, joined_A6$Gender)
table(joined$Q2, joined$Gender)

# Q1, Q2 -> Female sembrano un po' più sensibili 

table(joined$Q3, joined$Gender)
table(joined$Q4, joined$Gender)
table(joined$Q5, joined$Gender)
table(joined$Q6, joined$Gender)

# Q3, Q4, Q5, Q6 -> No diff 

table(joined$S1, joined$Gender)
table(joined$S2, joined$Gender)
table(joined$S3, joined$Gender)
table(joined$S4, joined$Gender)
table(joined$S5, joined$Gender)
table(joined$S6, joined$Gender)
table(joined$S7, joined$Gender)
table(joined$S8, joined$Gender)  
table(joined$S9, joined$Gender)
table(joined$S10, joined$Gender)

# S1-S10 no big differences