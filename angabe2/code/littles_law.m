(* ::Package:: *)

(* ::Title:: *)
(*Littles Gesetz*)


(* ::Text:: *)
(* Littles Theorem auf unsere Warteschlange*)


(* ::Subsection:: *)
(*Little Law*)


(* ::Input:: *)
(*\[Lambda] = 0.001;*)
(*\[Micro] = 0.01;*)
(*queue = QueueingProcess[\[Lambda],\[Micro]];*)
(*data = RandomFunction[queue,{0, 10000}];*)
(*ListStepPlot[data, PlotRange->Automatic]*)


(* ::Input:: *)
(*QueueProperties[queue]*)
(*L = QueueProperties[queue, "MeanSystemSize"]*)
(*\[Lambda]W = QueueProperties[queue, "ArrivalRate"]*QueueProperties[queue, "MeanSystemTime"]*)


(* ::Subsection:: *)
(*Eigene Resultate*)


csvpath = "./result.csv";
data = SemanticImport[FileNameJoin@{NotebookDirectory[],csvpath},CharacterEncoding->"UTF8"];
dataArrivals = data[Select[#Queuelength != ""&],<|
"t"->"Time",
"Q(t)"->"Queuelength"|>];
\[Micro] = 0.01;
ListStepPlot[dataArrivals]
(*Arrival Distribution for the Queueing Process*)
tmp = data[Select[#Time != ""&],<|
"t"->"Time"|>];
arrivalDistribution = Normal[tmp[[All,"t"]]];
ourqueue = QueueingProcess[arrivalDistribution,\[Micro]];
QueueProperties[ourqueue]


(* ::Subsection:: *)
(*Little Law mit 10facher Ankunftsrate*)


(* ::Input:: *)
(*\[Lambda] = 0.01;*)
(*\[Micro] = 0.01;*)
(*queue = QueueingProcess[\[Lambda],\[Micro]];*)
(*data = RandomFunction[queue, {0,10600}];*)
(*ListStepPlot[data, PlotRange->Automatic]*)


(* ::Input:: *)
(*QueueProperties[queue]*)
(*L = QueueProperties[queue, "MeanSystemSize"]*)
(*\[Lambda]W = QueueProperties[queue, "ArrivalRate"]*QueueProperties[queue, "MeanSystemTime"]*)


(* ::Text:: *)
(**)
