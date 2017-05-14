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
(*ACHTUNG :--\[Rule] HIER BITTE start new Simulation immer auf FALSE*)
startNewSimulation = false;
meanArrivalTime = 400;
meanServeTime = 100;
durationSimulation = 1000000;
vip = 10;
skip = 0;
configuration = 1;
lambda = 1/meanArrivalTime;
mu = 1/meanServeTime;
rho = lambda / mu;
(*Output directory: jeweils extra Ordner f\[UDoubleDot]r 1Phone, VIP, 2 Phone + VIP *)
outputDir =  FileNameJoin@{
NotebookDirectory[], StringJoin["../doku/abbildungen/1_Phone_Vergleich/Arrival_", ToString@meanArrivalTime, 
"_Serve_", ToString@meanServeTime, 
"_dur_", ToString@durationSimulation,
"_Skip_", ToString@skip, 
"/"]};
(*Wirft ein Fehler falls directory bereits vorhanden... ist aber OK so *)
CreateDirectory[outputDir, CreateIntermediateDirectories -> True]
(*DatenPfad erzeugt sich automatisch aus den Paramtern*)
dataPathVIP = StringJoin["../data/1_Phone_VIP_", ToString@vip ,"_Arrival_", ToString@meanArrivalTime, "_Skip_", ToString@skip]
dataPathNormal = StringJoin["../data/1_Phone_Arrival_", ToString@meanArrivalTime, "_Skip_", ToString@skip];
LoadNormal[name_]:= Import[FileNameJoin@{NotebookDirectory[],dataPathNormal, name <> ".csv"},HeaderLines->1];
LoadVIP[name_]:= Import[FileNameJoin@{NotebookDirectory[],dataPathVIP, name <> ".csv"},HeaderLines->1];


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
" -o " , dataPathNormal]
If[startNewSimulation == true, Simulate[arguments], ]


(* ::Section:: *)
(*Plot Mean QueueSize*)


dataPathVIP


Show[ListPlot[ {LoadNormal@"mean-queue-size-normal"}], 
Plot[Evaluate[y = MeanQueueSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}],
ListPlot[{LoadVIP@"mean-queue-size-all"},PlotStyle -> {Purple, Dashed, Thick}] ,
AxesLabel->{"t","mean queue size"}]
Export[FileNameJoin@{outputDir,"MeanQueueSize.pdf"},%];


Show[ListPlot[ {LoadNormal@"mean-system-size-normal"}], 
Plot[Evaluate[y = MeanSystemSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}],
ListPlot[{LoadVIP@"mean-system-size-all"},PlotStyle -> {Purple, Dashed, Thick}] ,
AxesLabel->{"t","mean system size"}]
Export[FileNameJoin@{outputDir,"MeanSystemSize.pdf"},%];


Show[ListPlot[ {LoadNormal@"mean-queue-time-normal"}], 
Plot[Evaluate[y = MeanQueueTimeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}],
ListPlot[{LoadVIP@"mean-queue-time-all"},PlotStyle -> {Purple, Dashed, Thick}] ,
AxesLabel->{"t","mean queue time"}]
Export[FileNameJoin@{outputDir,"MeanQueueTime.pdf"},%];


Show[ListPlot[ {LoadNormal@"mean-system-time-normal"}], 
Plot[Evaluate[y = MeanQueueSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}],
ListPlot[{LoadVIP@"mean-system-time-all"},PlotStyle -> {Purple, Dashed, Thick}] ,
AxesLabel->{"t","mean system time"}]
Export[FileNameJoin@{outputDir,"MeanSystemTime.pdf"},%];


(* ::Section:: *)
(*Theorem von Little : lambda * MeanSystemTime - MeanSystemSize = 0 f\[UDoubleDot]r t-> inf*)


(* ::Text:: *)
(*Im eingeschwungenen Zustand muss der Wert 0 betragen*)


Show[ ListPlot[LoadNormal@"little-system-normal"],  
ListPlot[LoadVIP@"little-system-all", PlotStyle -> {Purple, Dashed, Thick}],
{ AxesLabel->{"t","\!\(\*SubscriptBox[\(\[Lambda]M\), \(s\)]\) - \!\(\*SubscriptBox[\(L\), \(s\)]\)"}, Filling->Axis, PlotRange->Automatic}]
Export[FileNameJoin@{outputDir,"LittleSystem.pdf"},%];


(* ::Section:: *)
(*Ausgabe der Ergebnisse in Tabelle*)


(* ::Text:: *)
(*Achtung: hier wird ienfach der letzte Wert der Simulation verwendet*)


TableOfResults = Grid[{
{"value", "theoretical", "simulation without residents", "simulation with residents" },
{"mean queue size", N@MeanQueueSizeTheoretical, Last@Last[LoadNormal@"mean-queue-size-normal"], Last@Last[LoadVIP@"mean-queue-size-all"]},
{"mean queue time", N@MeanQueueTimeTheoretical, Last@Last[LoadNormal@"mean-queue-time-normal"], Last@Last[LoadVIP@"mean-queue-time-all"]},
{"mean system size", N@MeanSystemSizeTheoretical, Last@Last[LoadNormal@"mean-system-size-normal"], Last@Last[LoadVIP@"mean-system-size-all"]},
{"mean system time", N@MeanSystemTimeTheoretical, Last@Last[LoadNormal@"mean-system-time-normal"], Last@Last[LoadVIP@"mean-system-time-all"] }}]
Export[FileNameJoin@{outputDir,"ResultsTable.tex"},%, "TexFragment"];
