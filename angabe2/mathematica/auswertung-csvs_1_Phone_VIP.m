(* ::Package:: *)

SetDirectory[FileNameJoin@{NotebookDirectory[], "../code"}]
If[Run["mvn package"] == 1, "mvn nicht istalliert in Intellij maven target package ausf\[UDoubleDot]hren "]
Simulate[args_] := If[
	Run["java -jar " <> FileNameJoin@{NotebookDirectory[], "../code/target","queue-1.0-SNAPSHOT.jar "} <> args] == 1,
	"konnte simulation nicht ausf\[UDoubleDot]hren",
	"Simulation beendet"
]


(* ::Section:: *)
(*Parameter f\[UDoubleDot]r Simulation*)


(*Falls neue Simulation ansteuern wert auf true setzen *)
(*Falls wert auf false, dann wird keine neue Simulation durchgef\[UDoubleDot]hrt, sondern alte Daten herangezoogen *)
startNewSimulation = true;
meanArrivalTime = 400;
meanServeTime = 100;
durationSimulation = 1000000;
vip = 10;
skip = 0;
configuration = 2;
lambda = 1/meanArrivalTime;
mu = 1/meanServeTime;
rho = lambda / mu;
(*Output directory: jeweils extra Ordner f\[UDoubleDot]r 1Phone, VIP, 2 Phone + VIP *)
outputDir =  FileNameJoin@{
NotebookDirectory[], StringJoin["../doku/abbildungen/1_Phone_VIP/Arrival_", ToString@meanArrivalTime, 
"_Serve_", ToString@meanServeTime, 
"_dur_", ToString@durationSimulation,
"_Skip_", ToString@skip, 
"/"]};
(*Wirft ein Fehler falls directory bereits vorhanden... ist aber OK so *)
CreateDirectory[outputDir, CreateIntermediateDirectories -> True]
(*DatenPfad erzeugt sich automatisch aus den Paramtern*)
dataPath = StringJoin["../data/1_Phone_VIP_", ToString@vip ,"_Arrival_", ToString@meanArrivalTime, "_Skip_", ToString@skip]
Load[name_]:= Import[FileNameJoin@{NotebookDirectory[],dataPath, name <> ".csv"},HeaderLines->1];



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
" -d " , ToString@durationSimulation , 
" -vip " , ToString@vip , 
" -conf " , ToString@configuration , 
" -skip " , ToString@skip,
" -o " , dataPath]
If[startNewSimulation == true, Simulate[arguments], ]


(* ::Section:: *)
(*Plot Mean QueueSize*)


(* ::Subsubsection:: *)
(*Mean Queue Size*)


Show[ListPlot[ {Load@"mean-queue-size-normal"},
PlotRange->All], 
Plot[Evaluate[y = MeanQueueSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean queue size"}]
Export[FileNameJoin@{outputDir,"MeanQueueSizeNormal.pdf"},%];


Show[ListPlot[ {Load@"mean-queue-size-resident"},
PlotRange->All], 
Plot[Evaluate[y = MeanQueueSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean queue size"}]
Export[FileNameJoin@{outputDir,"MeanQueueSizeResident.pdf"},%];


Show[ListPlot[ {Load@"mean-queue-size-all"},
PlotRange->All], 
Plot[Evaluate[y = MeanQueueSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean queue size"}]
Export[FileNameJoin@{outputDir,"MeanQueueSizeAll.pdf"},%];


(* ::Subsubsection:: *)
(*Mean System Size*)


Show[ListPlot[ {Load@"mean-system-size-normal"},
PlotRange->All] , 
Plot[Evaluate[y = MeanSystemSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}],  
AxesLabel->{"t","mean system size"}]
Export[FileNameJoin@{outputDir,"MeanSystemSize.pdf"},%];


Show[ListPlot[ {Load@"mean-system-size-resident"},
PlotRange->All] , 
Plot[Evaluate[y = MeanSystemSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}],  
AxesLabel->{"t","mean system size"}]
Export[FileNameJoin@{outputDir,"MeanSystemSizeResident.pdf"},%];


Show[ListPlot[ {Load@"mean-system-size-all"},
PlotRange->All] , 
Plot[Evaluate[y = MeanSystemSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}],  
AxesLabel->{"t","mean system size"}]
Export[FileNameJoin@{outputDir,"MeanSystemSizeAll.pdf"},%];


(* ::Subsubsection:: *)
(*Mean Queue Time*)


Show[ListPlot[ {Load@"mean-queue-time-normal"},
PlotRange->All] , 
Plot[Evaluate[y = MeanQueueTimeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean queue time"}]
Export[FileNameJoin@{outputDir,"MeanQueueTime.pdf"},%];


Show[ListPlot[ {Load@"mean-queue-time-resident"},
PlotRange->All] , 
Plot[Evaluate[y = MeanQueueTimeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean queue time"}]
Export[FileNameJoin@{outputDir,"MeanQueueTimeResident.pdf"},%];


Show[ListPlot[ {Load@"mean-queue-time-all"},
PlotRange->All] , 
Plot[Evaluate[y = MeanQueueTimeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean queue time"}]
Export[FileNameJoin@{outputDir,"MeanQueueTimeAll.pdf"},%];


(* ::Subsubsection:: *)
(*Mean System Time*)


Show[ListPlot[ {Load@"mean-system-time-normal"},
PlotRange->All] , 
Plot[Evaluate[y = MeanSystemTimeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean system time"}]
Export[FileNameJoin@{outputDir,"MeanSystemTime.pdf"},%];


Show[ListPlot[ {Load@"mean-system-time-resident"},
PlotRange->All] , 
Plot[Evaluate[y = MeanSystemTimeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean system time"}]
Export[FileNameJoin@{outputDir,"MeanSystemTimeResident.pdf"},%];


Show[ListPlot[ {Load@"mean-system-time-all"},
PlotRange->All] , 
Plot[Evaluate[y = MeanSystemTimeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean system time"}]
Export[FileNameJoin@{outputDir,"MeanSystemTimeAll.pdf"},%];


(* ::Section:: *)
(*Queue Size StepPlot*)


queueSizeNormal = Load@"queue-size-normal";
  ListStepPlot[queueSizeNormal,Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotNormalAll.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[queueSizeNormal[[;;100]],Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotNormalFirst.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[queueSizeNormal[[-100;;]],Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotNormalLast.pdf"},%];


queueSizeResident = Load@"queue-size-resident";
  ListStepPlot[queueSizeResident,Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotResidentAll.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[queueSizeResident[[;;100]],Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotResidentFirst.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[queueSizeResident[[-100;;]],Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotResidentLast.pdf"},%];


queueSizeAll = Load@"queue-size-all";
  ListStepPlot[queueSizeAll,Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllAll.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[queueSizeAll[[;;100]],Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllFirst.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[queueSizeAll[[-100;;]],Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllLast.pdf"},%];


(* ::Section:: *)
(*Daten Filtern (Doppelte Zeitpunkte werden entfernt)*)


(* ::Text:: *)
(*Bei der Simulation werden doppelte Zeitpunkte ausgegeben: Kunde Kommt rein, Kunde f\[ADoubleDot]ngt an zu telefonieren.... Die Queue wird zuerst erh\[ODoubleDot]ht und dann wieder reduziert. In der Berechnung macht das keinen Unterschied, da der Zeitliche abstand zwischen  Erh\[ODoubleDot]hen und Reduzieren der Queue gleich 0 ist... In der Grafischen Darstellung macht es jedoch sinn, nur den letzten der Beiden Werte zu nehmen, da Wenn das Telefon leer ist, die Warteschlange nicht erh\[ODoubleDot]ht werden kann.*)


dataSet = queueSizeNormal;
(*Liste in verkehrter Reihenfolge sortieren*)
dataSet = Reverse[dataSet];
(*L\[ODoubleDot]sche alle Eintr\[ADoubleDot]ge mit der selben Zeit au\[SZ]er den ersten... Deswegen musste die Liste umgekehrt sortiert werden*)
dataSet = DeleteDuplicatesBy[dataSet, First];
(*Sortiere Liste wieder in die Urspr\[UDoubleDot]ngliche Form zur\[UDoubleDot]ck*)
dataSet = Reverse[dataSet];


ListStepPlot[dataSet,Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllFiltered.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[dataSet[[;;100]],Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotFirstFiltered.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[dataSet[[-100;;]],Filling->Bottom]
    Export[FileNameJoin@{outputDir,"QueueStepPlotLastFiltered.pdf"},%];


dataSet = queueSizeResident;
(*Liste in verkehrter Reihenfolge sortieren*)
dataSet = Reverse[dataSet];
(*L\[ODoubleDot]sche alle Eintr\[ADoubleDot]ge mit der selben Zeit au\[SZ]er den ersten... Deswegen musste die Liste umgekehrt sortiert werden*)
dataSet = DeleteDuplicatesBy[dataSet, First];
(*Sortiere Liste wieder in die Urspr\[UDoubleDot]ngliche Form zur\[UDoubleDot]ck*)
dataSet = Reverse[dataSet];


ListStepPlot[dataSet,Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllFiltered.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[dataSet[[;;100]],Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotFirstFiltered.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[dataSet[[-100;;]],Filling->Bottom]
    Export[FileNameJoin@{outputDir,"QueueStepPlotLastFiltered.pdf"},%];


dataSet = queueSizeAll;
(*Liste in verkehrter Reihenfolge sortieren*)
dataSet = Reverse[dataSet];
(*L\[ODoubleDot]sche alle Eintr\[ADoubleDot]ge mit der selben Zeit au\[SZ]er den ersten... Deswegen musste die Liste umgekehrt sortiert werden*)
dataSet = DeleteDuplicatesBy[dataSet, First];
(*Sortiere Liste wieder in die Urspr\[UDoubleDot]ngliche Form zur\[UDoubleDot]ck*)
dataSet = Reverse[dataSet];


ListStepPlot[dataSet,Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllFiltered.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[dataSet[[;;100]],Filling->Bottom]
  Export[FileNameJoin@{outputDir,"QueueStepPlotFirstFiltered.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[dataSet[[-100;;]],Filling->Bottom]
    Export[FileNameJoin@{outputDir,"QueueStepPlotLastFiltered.pdf"},%];


(* ::Section:: *)
(*Theorem von Little : lambda * MeanSystemTime - MeanSystemSize = 0 f\[UDoubleDot]r t-> inf*)


(* ::Text:: *)
(*Im eingeschwungenen Zustand muss der Wert 0 betragen*)


ListPlot[Load@"little-system-all"]
Export[FileNameJoin@{outputDir,"LittleSystem.pdf"},%];



