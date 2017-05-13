(* ::Package:: *)

SetDirectory[FileNameJoin@{NotebookDirectory[], "../code"}]
outputDir =  FileNameJoin@{NotebookDirectory[], "../doku/abbildungen/auswertung1000/"};
If[Run["mvn package"] == 1, "mvn nicht istalliert in Intellij maven target package ausf\[UDoubleDot]hren "]
Simulate[args_] := If[
	Run["java -jar " <> FileNameJoin@{NotebookDirectory[], "../code/target","queue-1.0-SNAPSHOT.jar "} <> args] == 1,
	"konnte simulation nicht ausf\[UDoubleDot]hren",
	"Simulation beendet"
]
Load[name_]:= Import[FileNameJoin@{NotebookDirectory[],"../data", name <> ".csv"},HeaderLines->1];






(* ::Section:: *)
(*Parameter f\[UDoubleDot]r Simulation*)


meanArrivalTime = 1000;
meanServeTime = 100;
duarationSimulation = 1000000;
vip = 0;
skip = 0;
configuration = 1;
lambda = 1/meanArrivalTime;
mu = 1/meanServeTime;
rho = lambda / mu;


(* ::Section:: *)
(*Theoretische Erwartungswerte laut M/M/1 Warteschlangenmodell*)


MeanSystemSizeTheoretical = lambda / (mu - lambda);
N[MeanSystemSizeTheoretical]


MeanQueueSizeTheoretical =  (rho * lambda) / (mu -lambda);
N[MeanQueueSizeTheoretical]


MeanSystemTimeTheoretical = 1 /(mu - lambda);
N[MeanSystemTimeTheoretical]


MeanQueueTimeTheoretical = rho / (mu -lambda);
N[MeanQueueTimeTheoretical]


(* ::InheritFromParent:: *)
(**)


(* umso n\[ADoubleDot]her a an c umso gr\[ODoubleDot]\[SZ]er skip probieren *)
arguments = StringJoin[
"-a " , ToString@meanArrivalTime , 
" -c " , ToString@meanServeTime , 
" -d " , ToString@duarationSimulation , 
" -vip " , ToString@vip , 
" -conf " , ToString@configuration , 
" -skip " , ToString@skip]
Simulate[arguments]
"Plots gefordert aus angabe:"
{
  ListPlot[{Load@"mean-queue-size-normal", Load@"mean-system-size-normal"},PlotRange->All],
  ListPlot[{Load@"mean-queue-time-normal", Load@"mean-system-time-normal"},PlotRange->All],
  ListPlot[ Load@"mean-system-time-normal",PlotRange->All],
  ListPlot[Load@"mean-phone-size-normal",PlotRange->All]
}

"Plot Anzahl in Queue"
queueSize = Load@"queue-size-normal";
{
  ListStepPlot[queueSize,Filling->Bottom],
  (* startphase anfang leer!*)
  ListStepPlot[queueSize[[;;100]],Filling->Bottom],
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[queueSize[[-100;;]],Filling->Bottom]
}

"Plots little"
{
  ListPlot[Load@"little-queue-normal",PlotRange->All],
  
}



(* ::Section:: *)
(*Plot Mean QueueSize*)


Show[ListPlot[ {Load@"mean-queue-size-normal"}], Plot[Evaluate[y = MeanQueueSizeTheoretical], {x, 0, duarationSimulation}]]


Show[ListPlot[ {Load@"mean-system-size-normal"}] , Plot[Evaluate[y = MeanSystemSizeTheoretical], {x, 0, duarationSimulation}]]


Show[ListPlot[ {Load@"mean-queue-time-normal"}] , Plot[Evaluate[y = MeanQueueTimeTheoretical], {x, 0, duarationSimulation}]]


Show[ListPlot[ {Load@"mean-system-time-normal"}] , Plot[Evaluate[y = MeanSystemTimeTheoretical], {x, 0, duarationSimulation}]]


(* ::Section:: *)
(*Queue Size StepPlot*)


queueSize = Load@"queue-size-normal";

  ListStepPlot[queueSize,Filling->Bottom]
  (* startphase anfang leer!*)
  ListStepPlot[queueSize[[;;100]],Filling->Bottom]
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[queueSize[[-100;;]],Filling->Bottom]






dataSet = queueSize;
dataSet = Reverse[dataSet];
dataSet = DeleteDuplicatesBy[dataSet, First];
dataSet = Reverse[dataSet];
ListStepPlot[dataSet,Filling->Bottom]
ListStepPlot[dataSet[[;;100]],Filling->Bottom]


(* ::Section:: *)
(*Theorem von Little : lambda * MeanSystemTime - MeanSystemSize = 0 f\[UDoubleDot]r t-> inf*)


ListPlot[Load@"little-system-normal"]
