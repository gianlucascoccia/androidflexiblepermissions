library(splitstackshape)
library(plyr)
library(vioplot)

filenames <- c(
  "Flickr",
  "OttawaInstr",
  "Wordpress",
  "FlickrInstr",
  "RunHelper",
  "WordpressInstr",
  "LocalWeather",
  "RunHelperInstr",
  "selfieHD",
  "LocalWeatherInstr",
  "Seatle",
  "selfieHDInstr",
  "Ottawa",
  "SeatleInstr"
)

fileName <- "cpu.pdf"

width <- 7.4
height <- 3.5 
margin <- 0.1

toPdf <- F

for(i in 1:length(filenames)) {
  assign(paste('x_', filenames[i], sep=''), read.csv(paste("./merged/", filenames[i], ".csv", sep = ""), sep=",", header=T, row.names=NULL, stringsAsFactors=FALSE))
}

if(toPdf) {
  setupPdf()
}

analysis <- function() {
  labels <- c("a1", "a1 instr.", "a2", "a2 instr.", "a3", "a3 instr.", "a4", "a4 instr.", "a5", "a5 instr.", "a6", "a6 instr.", "a7", "a7 instr.")
  par(las=2)
  p <- boxplot(x_Wordpress$cpu,
               x_WordpressInstr$cpu,
               x_Ottawa$cpu,
               x_OttawaInstr$cpu,
               x_Seatle$cpu,
               x_SeatleInstr$cpu,
               x_LocalWeather$cpu,
               x_LocalWeatherInstr$cpu,
               x_RunHelper$cpu,
               x_RunHelperInstr$cpu,
               x_selfieHD$cpu,
               x_selfieHDInstr$cpu,
               x_Flickr$cpu,
               x_FlickrInstr$cpu,
               names=labels, col="grey", drawRect=TRUE, wex=0.5, range=1)
    abline(v=2.5)
    abline(v=4.5)
    abline(v=6.5)
    abline(v=8.5)
    abline(v=10.5)
    abline(v=12.5)
  # text(y=c(10,10,10,10,10,10,10,10,10,10,10,10,10,10), labels=c(round(mean(x_Wordpress$cpu), 2),
  #                                   round(mean(x_WordpressInstr$cpu), 2),
  #                                   round(mean(x_Ottawa$cpu), 2),
  #                                   round(mean(x_OttawaInstr$cpu), 2),
  #                                   round(mean(x_Seatle$cpu), 2),
  #                                   round(mean(x_SeatleInstr$cpu), 2),
  #                                   round(mean(x_LocalWeather$cpu), 2),
  #                                   round(mean(x_LocalWeatherInstr$cpu), 2),
  #                                   round(mean(x_RunHelper$cpu), 2),
  #                                   round(mean(x_RunHelperInstr$cpu), 2),
  #                                   round(mean(x_selfieHD$cpu), 2),
  #                                   round(mean(x_selfieHDInstr$cpu), 2),
  #                                   round(mean(x_Flickr$cpu), 2),
  #                                   round(mean(x_FlickrInstr$cpu), 2)), x = c(1,2,3,4,5,6,7,8,9,10,11,12,13,14), col="red")
}

check_p_values_cpu <- function() {
  print(wilcox.test(x_Wordpress$cpu,x_WordpressInstr$cpu, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_Ottawa$cpu,x_OttawaInstr$cpu, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_Seatle$cpu,x_SeatleInstr$cpu, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_LocalWeather$cpu,x_LocalWeatherInstr$cpu, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_RunHelper$cpu,x_RunHelperInstr$cpu, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_selfieHD$cpu,x_selfieHDInstr$cpu, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_Flickr$cpu,x_FlickrInstr$cpu, correction=F, alternative="l")$p.value)
}

check_p_values_heap <- function() {
  print(wilcox.test(x_Wordpress$heap,x_WordpressInstr$heap, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_Ottawa$heap,x_OttawaInstr$heap, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_Seatle$heap,x_SeatleInstr$heap, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_LocalWeather$heap,x_LocalWeatherInstr$heap, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_RunHelper$heap,x_RunHelperInstr$heap, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_selfieHD$heap,x_selfieHDInstr$heap, correction=F, alternative="l")$p.value)
  print(wilcox.test(x_Flickr$heap,x_FlickrInstr$heap, correction=F, alternative="l")$p.value)
}

#analysis()

if(toPdf) {
  dev.off()
}

setupPdf <- function() {
  margin <- margin
  pdf(fileName, width=width, height=height)
  par(mar=c(3, 3, 3, margin))
  par(mfrow=c(1, 1))
  par(las=1)
}

check_p_values_cpu()

