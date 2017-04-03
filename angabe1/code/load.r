load.geschwindigkeiten <- function () {
	d = read.csv(file="../data/geschwindigkeiten.csv", head=TRUE, sep=",")
	return(d)
}

load.probanden <- function() {
	d = read.csv(file="../data/probanden.csv", head=TRUE, sep=",")
	return(d)
}

# L�dt die gro�e Tabelle mit allen Werten kombiniert.
load.full <- function() {
	m = merge(load.geschwindigkeiten(), load.probanden())
	return(m)
}