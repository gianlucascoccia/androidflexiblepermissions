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

fileName <- "heap.pdf"

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
  p <- boxplot(x_Wordpress$heap / 1024,
               x_WordpressInstr$heap / 1024,
               x_Ottawa$heap / 1024,
               x_OttawaInstr$heap / 1024,
               x_Seatle$heap / 1024,
               x_SeatleInstr$heap / 1024,
               x_LocalWeather$heap / 1024,
               x_LocalWeatherInstr$heap / 1024,
               x_RunHelper$heap / 1024,
               x_RunHelperInstr$heap / 1024,
               x_selfieHD$heap / 1024,
               x_selfieHDInstr$heap / 1024,
               x_Flickr$heap / 1024,
               x_FlickrInstr$heap / 1024,
               names=labels, col="grey", drawRect=TRUE, wex=0.5, range=1)
    abline(v=2.5)
    abline(v=4.5)
    abline(v=6.5)
    abline(v=8.5)
    abline(v=10.5)
    abline(v=12.5)
  # text(y=c(200,200,200,200,200,200,200,200,200,200,200,200,200,200), labels=c(round(mean(x_Wordpress$heap), 0) / 1024,
  #                                   round(mean(x_WordpressInstr$heap), 2) / 1024,
  #                                   round(mean(x_Ottawa$heap), 2) / 1024,
  #                                   round(mean(x_OttawaInstr$heap), 2) / 1024,
  #                                   round(mean(x_Seatle$heap), 2) / 1024,
  #                                   round(mean(x_SeatleInstr$heap), 2) / 1024,
  #                                   round(mean(x_LocalWeather$heap), 2) / 1024,
  #                                   round(mean(x_LocalWeatherInstr$heap), 2) / 1024,
  #                                   round(mean(x_RunHelper$heap), 2) / 1024,
  #                                   round(mean(x_RunHelperInstr$heap), 2) / 1024,
  #                                   round(mean(x_selfieHD$heap), 2) / 1024,
  #                                   round(mean(x_selfieHDInstr$heap), 2) / 1024,
  #                                   round(mean(x_Flickr$heap), 2) / 1024,
  #                                   round(mean(x_FlickrInstr$heap), 2) / 1024), x = c(1,2,3,4,5,6,7,8,9,10,11,12,13,14), col="red")
}

analysis()

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
