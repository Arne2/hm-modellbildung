(* ::Package:: *)

(* ::Text:: *)
(*Import Data *)


(* ::Input:: *)
(*streckeEbene = 27.3;*)


(* ::Input:: *)
(*dataPath="../data/2017/Messungen_2017_Geschwindigkeit_Ebene.csv";*)
(*dataEbene=SemanticImport@FileNameJoin@{NotebookDirectory[],dataPath}*)


(* ::Input:: *)
(**)


(* ::Input:: *)
(*dataEbeneMitGeschw = dataEbene[All,Append[#,"Geschwindigkeit"->streckeEbene /#Zeit]&]*)


(* ::Section:: *)
(*Histogramm*)


(* ::Text:: *)
(*Es wird ein Histogramm erstellt und einer Normalverteilung gegen\[UDoubleDot]bergestellt. F\[UDoubleDot]r die Kurver der Normalverteilung muss der Erwartungswert und die Standardabweichung berechnet werden.*)


(* ::Text:: *)
(*Berechnung des Mittelwertes und der Standardabweichung*)


(* ::Input:: *)
(*geschwMean = Mean[dataEbeneMitGeschw[All, "Geschwindigkeit"]]*)


(* ::Input:: *)
(*geschwDeviation = StandardDeviation[dataEbeneMitGeschw[All, "Geschwindigkeit"]]*)


(* ::Text:: *)
(*Gegen\[UDoubleDot]berstellung von H\[ADoubleDot]ufigkeitsverteilung und Normalverteilung. *)
(*ACHTUNG: Das Histogramm wurde mit der Option "PDF" erstellt. Das bedeutet, dass nicht die absoluten H\[ADoubleDot]ufigkeiten sondern die H\[ADoubleDot]ufigkeitsdichte dargstellt ist, Damit l\[ADoubleDot]sst es sich besser mit dem Graphen der Normalverteilung vergleichen. Die Darstellung ist dann nicht mehr von der Anzahl der s\[ADoubleDot]ulen abh\[ADoubleDot]ngig.*)
(*Bemerkungen wie getr\[ODoubleDot]delt wurden ignoriert. Es wird angenommen, dass im Schnitt es immer Personen gibt, die langsamer oder schneller gehen. Die Probanden hatten keine Vorgabe eine bestimmte Geschwindigkeit einzuhalten. Somit sind auch Probanden zu ber\[UDoubleDot]cksichtigen, die langsamer gehen. Es ist jedoch anzumerken, dass bei einem Versuch mit nur einer geringen Anzahl von Probanden, solche Sonderheiten eventuell eine signifikante Abweichung verursachen.*)


(* ::Input:: *)
(*Show[Histogram[dataEbeneMitGeschw[All, "Geschwindigkeit"],10, "PDF"],Plot[PDF[NormalDistribution[geschwMean,geschwDeviation],x],{x,1.1,1.75},PlotStyle->Thick],AxesLabel->{HoldForm[v[m/s]],HoldForm[rel.H\[ADoubleDot]ufigkeit]}]*)


(* ::Section:: *)
(*Quantileplot*)


(* ::Input:: *)
(*QuantilePlot[dataEbeneMitGeschw[All, "Geschwindigkeit"]]*)


(* ::Section:: *)
(*Distribution Fit Test*)


(* ::Input:: *)
(*DistTestEbene=DistributionFitTest[Array[dataEbeneMitGeschw[All, "Geschwindigkeit"], 66],NormalDistribution[geschwMean,geschwDeviation],"HypothesisTestData"]*)


(* ::Input:: *)
(*DistTestEbene["TestDataTable",All]*)


(* ::Text:: *)
(*Der Distribution Fit Test ergibt, dass die Geschwindigkeiten normalverteilt sind. Laut dem Cramer-von Mises Test wird die 5% H\[UDoubleDot]rde \[UDoubleDot]berschritten.*)
(*Die Geschwindigkeiten auf der Ebene (Wunschgeschwindigkeiten) sind normalverteilt*)
