(* ::Package:: *)

(* ::Input:: *)
(*dataPath = FileNameJoin@{NotebookDirectory[],"../data/arrival-delta.csv"};*)
(*outputDir =  FileNameJoin@{NotebookDirectory[], "../doku/abbildungen/distribution/"};*)
(*dataRand=Transpose[Import[dataPath, HeaderLines->1]][[1]]*)
(*lambda = 1/400;*)


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
