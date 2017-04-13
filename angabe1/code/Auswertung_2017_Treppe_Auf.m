(* ::Package:: *)

(* ::Text:: *)
(*Import Data *)


(* ::Input:: *)
(*streckeTreppe = 9.39;*)


(* ::Input:: *)
(*dataPath="../data/2017/Messungen_2017_Geschwindigkeit_Treppe_Auf.csv";*)
(*dataTreppeAuf=SemanticImport@FileNameJoin@{NotebookDirectory[],dataPath}*)


(* ::Input:: *)
(**)


(* ::Input:: *)
(*dataTreppeMitGeschw = dataTreppeAuf[All,Append[#,"Geschwindigkeit"->streckeTreppe/#Zeit]&]*)


(* ::Section:: *)
(*Histogramm*)


(* ::Text:: *)
(*Es wird ein Histogramm erstellt und einer Normalverteilung gegen\[UDoubleDot]bergestellt. F\[UDoubleDot]r die Kurve der Normalverteilung muss der Erwartungswert und die Standardabweichung berechnet werden.*)


(* ::Text:: *)
(*Berechnung des Mittelwertes und der Standardabweichung*)


(* ::Input:: *)
(*geschwMean = Mean[dataTreppeMitGeschw[All, "Geschwindigkeit"]]*)


(* ::Input:: *)
(*geschwDeviation = StandardDeviation[dataTreppeMitGeschw[All, "Geschwindigkeit"]]*)


(* ::Text:: *)
(*Gegen\[UDoubleDot]berstellung von H\[ADoubleDot]ufigkeitsverteilung und Normalverteilung. *)
(*ACHTUNG: Das Histogramm wurde mit der Option "PDF" erstellt. Das bedeutet, dass nicht die absoluten H\[ADoubleDot]ufigkeiten sondern die H\[ADoubleDot]ufigkeitsdichte dargstellt ist, Damit l\[ADoubleDot]sst es sich besser mit dem Graphen der Normalverteilung vergleichen. Die Darstellung ist dann nicht mehr von der Anzahl der s\[ADoubleDot]ulen abh\[ADoubleDot]ngig.*)
(*Bemerkungen wie getr\[ODoubleDot]delt wurden ignoriert. Es wird angenommen, dass im Schnitt es immer Personen gibt, die langsamer oder schneller gehen. Die Probanden hatten keine Vorgabe eine bestimmte Geschwindigkeit einzuhalten. Somit sind auch Probanden zu ber\[UDoubleDot]cksichtigen, die langsamer gehen. Es ist jedoch anzumerken, dass bei einem Versuch mit nur einer geringen Anzahl von Probanden, solche Sonderheiten eventuell eine signifikante Abweichung verursachen.*)
(**)
(**)
(*ACHTUNG: Die Ausreisser in diesem Datensatz sind periodisch. Das hei\[SZ]t es gibt 4 Personen, welche immer schneller gehen, als der Rest. Es ist anzunehmen, dass dies die Auswertung stark beeinflussen wird.*)


(* ::Input:: *)
(*Show[Histogram[dataTreppeMitGeschw[All, "Geschwindigkeit"],10, "PDF"],Plot[PDF[NormalDistribution[geschwMean,geschwDeviation],x],{x,0.6,1.4},PlotStyle->Thick], AxesLabel->{HoldForm[(v m)/s],HoldForm[rel.H\[ADoubleDot]ufigkeit]}]*)


(* ::Section:: *)
(*Quantileplot*)


(* ::Input:: *)
(*QuantilePlot[dataTreppeMitGeschw[All, "Geschwindigkeit"]]*)


(* ::Section:: *)
(*Distribution Fit Test*)


(* ::Input:: *)
(*DistTestTreppe=DistributionFitTest[Array[dataTreppeMitGeschw[All, "Geschwindigkeit"], 66],NormalDistribution[geschwMean,geschwDeviation],"HypothesisTestData"]*)


(* ::Input:: *)
(*DistTestTreppe["TestDataTable",All]*)


(* ::Text:: *)
(*Der Distribution Fit Test ergibt, dass die Geschwindigkeiten NICHT normalverteilt sind. Laut dem Cramer-von Mises Test wird die 5% H\[UDoubleDot]rde nicht \[UDoubleDot]berschritten.*)
(*Die Geschwindigkeiten Beim Raufgehen der Treppen sind nicht normalverteilt.*)


(* ::Section:: *)
(*Ausrei\[SZ]er entfernen*)


(* ::Text:: *)
(*Es wurde festgestellt, dass die Geschwindigkeiten nicht normalverteilt sind. Es wird untersucht, ob die Ausrei\[SZ]er einen signifikanten Einfluss aus\[UDoubleDot]ben.*)


(* ::Input:: *)
(*dataOhneAusreisser = dataTreppeMitGeschw[Select[#"Bemerkung"==""&], "Geschwindigkeit"]*)


(* ::Input:: *)
(*geschwNeuMean = Mean[dataOhneAusreisser]*)


(* ::Input:: *)
(*geschwNeuDeviation = StandardDeviation[dataOhneAusreisser]*)


(* ::Input:: *)
(*Show[Histogram[dataOhneAusreisser,10, "PDF"],Plot[PDF[NormalDistribution[geschwNeuMean,geschwNeuDeviation],x],{x,0.65,0.96},PlotStyle->Thick],AxesLabel->{HoldForm[(v m)/s],HoldForm[rel.H\[ADoubleDot]ufigkeit]}]*)


(* ::Input:: *)
(*QuantilePlot[dataOhneAusreisser]*)


(* ::Input:: *)
(*DistTestTreppeOhneAusreisser=DistributionFitTest[Array[dataOhneAusreisser, 54],NormalDistribution[geschwNeuMean,geschwNeuDeviation],"HypothesisTestData"]*)


(* ::Input:: *)
(*DistTestTreppeOhneAusreisser["TestDataTable",All]*)


(* ::Text:: *)
(*Es ist festzustellen, dass die Geschwindigkeiten normalverteilt sind, wenn die Ausrei\[SZ]er nicht ber\[UDoubleDot]cksichtigt werden. F\[UDoubleDot]r sp\[ADoubleDot]tere betrachtung muss entschieden werden, ob diese Ausreisser in die Auswertung mit einflie\[SZ]en oder nicht. *)
