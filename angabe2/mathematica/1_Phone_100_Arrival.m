(* ::Package:: *)

(* ::Title:: *)
(*Littles Gesetz*)


(* ::Text:: *)
(* Littles Theorem auf unsere Warteschlange*)


(* ::Subsection:: *)
(*Little Law*)


(* ::Input:: *)
(*\[Lambda] = 0.01;*)
(*\[Micro] = 0.01;*)
(*queue = QueueingProcess[\[Lambda],\[Micro]];*)
(*data = RandomFunction[queue,{0, 10000}];*)
(*ListStepPlot[data, PlotRange->Automatic]*)


(* ::Input:: *)
(*QueueProperties[queue]*)
(*L = QueueProperties[queue, "MeanSystemSize"]*)
(*\[Lambda]W = QueueProperties[queue, "ArrivalRate"]*QueueProperties[queue, "MeanSystemTime"]*)


(* ::Subsection:: *)
(*Eigene Resultate (mittlere Ankunftszeit =  100s)*)


SetDirectory[FileNameJoin@{NotebookDirectory[], "../code"}]
outputDir =  FileNameJoin@{NotebookDirectory[], "../doku/abbildungen/auswertung1000/"};
If[Run["mvn package"] == 1, "mvn nicht istalliert in Intellij maven target package ausf\[UDoubleDot]hren "]

args = " ";
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


dataQueuePath = FileNameJoin@{NotebookDirectory[],"../data/queue.csv"};
dataQueue = SemanticImport[dataQueuePath];
dataQueue = dataQueue[Select[#room == 0 || #room != #size& ]];
dataQueuePlot = Drop[dataQueue, None, {3}];
ListStepPlot[dataQueuePlot]
Export[FileNameJoin@{outputDir,"queueLengthPlot.pdf"},%];


d = Import[FileNameJoin@{NotebookDirectory[],"../data", "queue" <> ".csv"},HeaderLines->1];


AreaQueueSize[data_, n_]:=data[[n,2]] * (data[[n+1,1]] - data[[n,1]])
MeanQueueSize[d_] := 
	Sum[AreaQueueSize[d,t], {t, 1 ,Length@d - 1  }] / (Last@d)[[1]]
	
AreaRoomSize[data_, n_]:=data[[n,3]] * (data[[n+1,1]] - data[[n,1]])
MeanRoomSize[d_] := 
	Sum[AreaRoomSize[d,t], {t, 1 ,Length@d - 1  }] / (Last@d)[[1]]


meanQueueSizeValue = MeanQueueSize[d]


meanRoomSizeValue = MeanRoomSize[d]


MeanQueueTime[time_]:=Mean@data[Select[#begin <= time&],#begin - #arrival&]
lastTime=Max@data[All,"begin"];
DiscretePlot[MeanQueueTime[x], {x,1,lastTime, lastTime/100}]
Export[FileNameJoin@{outputDir,"meanQueueTimePlot.pdf"},%];


meanQueueTimeValue = MeanQueueTime[lastTime]


MeanCallingTime[time_]:=Mean@data[Select[#finish <= time&],#finish - #begin&]
lastTime=Max@data[All,"finish"];
DiscretePlot[MeanCallingTime[x], {x,1,lastTime, lastTime/100}]
Export[FileNameJoin@{outputDir,"meanCallingTimePlot.pdf"},%];


DiscretePlot[{data[x,"time"],Mean@data[1;;x,#finish - #begin&]}, {x,1,2000}]


meanCallingtimeValue = MeanCallingTime[lastTime]


MeanSystemTime[time_]:=Mean@data[Select[#finish < time&],#finish - #arrival&]
lastTime=Max@data[All,"finish"];
DiscretePlot[MeanSystemTime[x], {x,1,lastTime, lastTime/100}]
Export[FileNameJoin@{outputDir,"meanSystemTimePlot.pdf"},%];


meanSystemTimeValue = MeanSystemTime[lastTime]


TableOfResults = Grid[{
{"mean queue size", meanQueueSizeValue},
{"mean queue time", meanQueueTimeValue},
{"mean system size", meanRoomSizeValue},
{"mean system time", meanSystemTimeValue}}]
Export[FileNameJoin@{outputDir,"ResultsTable.tex"},%, "TexFragment"];


LittleFunction[x_]:=\[Lambda]*MeanSystemTime[x] - MeanRoomSize[d]


DiscretePlot[LittleFunction[x], {x,1,lastTime, lastTime/100}]
Export[FileNameJoin@{outputDir,"LittlePlot.pdf"},%];
