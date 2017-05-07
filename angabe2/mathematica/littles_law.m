(* ::Package:: *)

(* ::Title:: *)
(*Littles Gesetz*)


(* ::Text:: *)
(* Littles Theorem auf unsere Warteschlange*)


(* ::Subsection::Closed:: *)
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


SetDirectory[FileNameJoin@{NotebookDirectory[], "../code"}]

If[Run["mvn package"] == 1, "mvn nicht istalliert in Intellij maven target package ausf\[UDoubleDot]hren "]

args = " 1000 100 0 1";
If[
	Run["java -jar " <> FileNameJoin@{NotebookDirectory[], "../code/target","queue-1.0-SNAPSHOT.jar"} <> args] == 1,
	"konnte simulation nicht ausf\[UDoubleDot]hren",
	"Simulation beendet"
]


Load[type_] := Module[{data},
	data = SemanticImport[FileNameJoin@{NotebookDirectory[],"../data", type <> ".csv"},HeaderLines->1];
	data[All, <| "id"->"id", "resident"->"resident",type->"time"|>]
]

data = JoinAcross[
	JoinAcross[Load["arrival"],Load["begin"],{"id"}],
	Load["finish"],{"id"}
];


f[time_]:=Mean@data[Select[#begin < time&],#begin - #arrival&]
lastTime=Max@data[All,"begin"];
DiscretePlot[f[x], {x,1,lastTime, lastTime/100}]


f[time_]:=Mean@data[Select[#finish < time&],#finish - #begin&]
lastTime=Max@data[All,"finish"];
DiscretePlot[f[x], {x,1,lastTime, lastTime/100}]


f[time_]:=Mean@data[Select[#finish < time&],#finish - #arrival&]
lastTime=Max@data[All,"finish"];
DiscretePlot[f[x], {x,1,lastTime, lastTime/100}]


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


d = Import[FileNameJoin@{NotebookDirectory[],"../data", "queue" <> ".csv"},HeaderLines->1]
queue


AreaQueueSize[data_, n_]:=data[[n,2]] * (data[[n+1,1]] - data[[n,1]])
MeanQueueSize[d_] := 
	Sum[AreaQueueSize[d,t], {t, 1 ,Length@d - 1  }] / (Last@d)[[1]]
	
AreaRoomSize[data_, n_]:=data[[n,3]] * (data[[n+1,1]] - data[[n,1]])
MeanQueueSize[d_] := 
	Sum[AreaRoomSize[d,t], {t, 1 ,Length@d - 1  }] / (Last@d)[[1]]


MeanQueueSize[d]


AreaRoomSize[data_, n_]:=data[[n,3]] * (data[[n+1,1]] - data[[n,1]])
Sum[AreaRoomSize[d,t], {t, 1 ,Length@d - 1  }] / (Last@d)[[1]]
