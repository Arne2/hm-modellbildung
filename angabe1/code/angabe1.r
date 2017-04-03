# Mit Strg + R hintereinander ausführen workspace muss dieser ordner sein

source("load.r")

data = load.full()
ebene = data[data$Ebene == "x", ]
auf = data[data$Treppe == "auf", ]
ab = data[data$Treppe == "ab", ]

# Histogramm
hist(ebene$Zeit.in.sec)

# Boxplot
boxplot(ebene$Zeit.in.sec, auf$Zeit.in.sec, ab$Zeit.in.sec)

# Quantile Plot
qqnorm(ebene$Zeit.in.sec)
qqline(ebene$Zeit.in.sec)

# Lineare Regression
m1 <- lm(auf$Zeit.in.sec ~ ebene$Runde)
summary(m1)
