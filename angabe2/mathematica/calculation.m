(* ::Package:: *)

(* ::Input:: *)
(*SetDirectory[FileNameJoin@{NotebookDirectory[], "../data"}]*)
(*outputDir =  FileNameJoin@{NotebookDirectory[], "../doku/abbildungen/auswertungCalc/"};*)
(**)


(* ::Input:: *)
(*dataCalcPath = FileNameJoin@{NotebookDirectory[],"../data/calculation.csv"};*)
(*dataCalc = SemanticImport[dataCalcPath];*)


dataCalc


meanQueueSize = dataCalc[[All, {"time","meanQueueSize"}]]


ListPlot[meanQueueSize]


(* ::InheritFromParent:: *)
(*ListPlot[dataCalc[[All, {"time", "meanSystemSize"}]]]*)


ListPlot[dataCalc[[All, {"time", "meanWaitingTime"}]]]


ListPlot[dataCalc[[All, {"time", "meanCallingTime"}]]]


ListPlot[dataCalc[[All, {"time", "meanSystemTime"}]]]


ListPlot[dataCalc[[All, {"time", "LittlesLaw"}]]]


dataQueue = dataCalc[Select[#systemSize == 0 || #systemSize != #queueSize& ]]


ListStepPlot[dataQueue[[All,{"time","queueSize"}]]]
