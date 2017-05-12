(* ::Package:: *)

(* ::Input:: *)
(*dataPath = FileNameJoin@{NotebookDirectory[],"../data/arrival_distribution.csv"};*)
(*outputDir =  FileNameJoin@{NotebookDirectory[], "../doku/abbildungen/distribution/"};*)
(*dataRand=SemanticImport[dataPath];*)
(*lambda = 0.001;*)


(* ::Input:: *)
(**)


(* ::Chapter:: *)
(*Histogram*)


(* ::Input:: *)
(*Show[Histogram[dataRand],AxesLabel->{HoldForm[t[s]],HoldForm[rel.H\[ADoubleDot]ufigkeit]}] *)
(*Export[FileNameJoin@{outputDir,"histogramm.pdf"},%];*)


(* ::Input:: *)
(*Show[Histogram[dataRand,50, "PDF"],Plot[PDF[ExponentialDistribution[lambda],x],{x,0,5000},PlotStyle->Thick],AxesLabel->{HoldForm[t[s]],HoldForm[rel.H\[ADoubleDot]ufigkeit]}] *)
(*Export[FileNameJoin@{outputDir,"histogrammExpVerteilung.pdf"},%];*)


(* ::Chapter:: *)
(*QuantilePlot*)


(* ::Input:: *)
(*QuantilePlot[dataRand, ExponentialDistribution[lambda]]*)
(*Export[FileNameJoin@{outputDir,"quantilePlot.pdf"},%];*)


(* ::Chapter:: *)
(*Distribution Test*)


(* ::Input:: *)
(*distTest=DistributionFitTest[Array[dataRand, 100000], ExponentialDistribution[lambda],"HypothesisTestData"]*)
(*Export[FileNameJoin@{outputDir,"dist_fit_test.tex"},%, "TexFragment"];*)


(* ::Input:: *)
(**)


(* ::Input:: *)
(*distTest["TestDataTable",All]*)
(*Export[FileNameJoin@{outputDir,"dis_fit_test_table.tex"},%, "TexFragment"];*)


(* ::Input:: *)
(**)


(* ::Chapter:: *)
(*Mean  & Median*)


(* ::Input:: *)
(*Mean[dataRand]*)


(* ::Input:: *)
(*Median[dataRand]*)
