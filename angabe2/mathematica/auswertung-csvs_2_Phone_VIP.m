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
startNewSimulation = false;
meanArrivalTime = 400;
meanServeTime = 100;
durationSimulation = 1000000;
vip = 10;
skip = 0;
configuration = 3;
numberOfPhones = 2;
lambda = 1/meanArrivalTime;
mu = 1/meanServeTime;
rho = lambda / mu;
(*Output directory: jeweils extra Ordner f\[UDoubleDot]r 1Phone, VIP, 2 Phone + VIP *)
outputDir =  FileNameJoin@{
NotebookDirectory[], StringJoin["../doku/abbildungen/2_Phone_VIP/Arrival_", ToString@meanArrivalTime, 
"_Serve_", ToString@meanServeTime, 
"_dur_", ToString@durationSimulation,
"_Skip_", ToString@skip, 
"/"]};
(*Wirft ein Fehler falls directory bereits vorhanden... ist aber OK so *)
CreateDirectory[outputDir, CreateIntermediateDirectories -> True]
(*DatenPfad erzeugt sich automatisch aus den Paramtern*)
dataPath = StringJoin["../data/2_Phone_VIP_", ToString@vip ,"_Arrival_", ToString@meanArrivalTime, "_Skip_", ToString@skip]
Load[name_]:= Import[FileNameJoin@{NotebookDirectory[],dataPath, name <> ".csv"},HeaderLines->1];



(* ::Section:: *)
(*Theoretische Erwartungswerte laut M/M/s Warteschlangenmodell*)


(* ::Text:: *)
(*Links zu den Dokumenten mit Formeln:*)
(*https://www.google.de/url?sa=t&rct=j&q=&esrc=s&source=web&cd=3&cad=rja&uact=8&ved=0ahUKEwjW6tSPsO_TAhUG1RoKHeeFACEQFgg4MAI&url=http%3A%2F%2Ffaculty.ksu.edu.sa%2F9766%2FOR372%2FLec13_MMs_Queueing%2520System2.pdf&usg=AFQjCNHgfbvYXBn9EUGB2iAHBZEyVCZsVg*)
(**)
(*https://en.wikipedia.org/wiki/M/M/c_queue*)
(*http://personal.maths.surrey.ac.uk/st/K.Young/Modules/MS334/M-M-c.pdf   <--- erste Formel f\[UDoubleDot]r P0... Andere Formeln scheinen falsch zu sein*)
(*http://www.supositorio.com/rcalc/rcalclite.htm    <---- Online rechner f\[UDoubleDot]r Warteschlangen*)
(**)


(*Beide Formeln f\[UDoubleDot]r P0 f\[UDoubleDot]hren zum selben Ergebnis *)
Pzero = (1+rho+rho^2/(2-rho))^-1;
N[Pzero]


(*Anzahl der server *)
s = numberOfPhones


Pzero = ((lambda^s)/(s!*mu^s)*(s*mu)/(s*mu-lambda)+Sum[(lambda^i)/(i!*mu^i), {i, 0, s-1}])^-1;
N[Pzero]


MeanSystemSizeTheoretical = rho + ((rho^(s+1))/((s-rho)!(s-rho)^2)) * Pzero;
N[MeanSystemSizeTheoretical]


MeanQueueSizeTheoretical =  (Pzero * rho^(2+1)) / ((2-1)!(2-rho)^2);
N[MeanQueueSizeTheoretical]


MeanSystemTimeTheoretical = MeanSystemSizeTheoretical / lambda;
N[MeanSystemTimeTheoretical]


MeanQueueTimeTheoretical = MeanQueueSizeTheoretical / lambda;
N[MeanQueueTimeTheoretical]


(* ::InheritFromParent:: *)
(**)


(* umso n\[ADoubleDot]her a an c umso gr\[ODoubleDot]\[SZ]er skip probieren *)
arguments = StringJoin[
" -a " , ToString@meanArrivalTime , 
" -c " , ToString@meanServeTime , 
" -d " , ToString@durationSimulation , 
" -vip " , ToString@vip , 
" -conf " , ToString@configuration , 
" -skip " , ToString@skip,
" -o " , dataPath]
If[startNewSimulation == true, Simulate[arguments], ]


(* ::Section:: *)
(*Plot Mean QueueSize / SystemSize / QueueTime / SystemTime*)


(* ::Subsubsection:: *)
(*Mean Queue Size*)


Show[ListPlot[ {Load@"mean-queue-size-normal"}], 
AxesLabel->{"t","mean queue size tourist"}]
Export[FileNameJoin@{outputDir,"MeanQueueSizeNormal.pdf"},%];


Show[ListPlot[ {Load@"mean-queue-size-resident"}],  
AxesLabel->{"t","mean queue size resident"}]
Export[FileNameJoin@{outputDir,"MeanQueueSizeResident.pdf"},%];


Show[ListPlot[ {Load@"mean-queue-size-all"}], 
Plot[Evaluate[y = MeanQueueSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean queue size all"}]
Export[FileNameJoin@{outputDir,"MeanQueueSizeAll.pdf"},%];


(* ::Subsubsection::Closed:: *)
(*Mean System Size*)


Show[ListPlot[ {Load@"mean-system-size-normal"}],  
AxesLabel->{"t","mean system size tourist"}]
Export[FileNameJoin@{outputDir,"MeanSystemSize.pdf"},%];


Show[ListPlot[ {Load@"mean-system-size-resident"}],  
AxesLabel->{"t","mean system size resident"}]
Export[FileNameJoin@{outputDir,"MeanSystemSizeResident.pdf"},%];


Show[ListPlot[ {Load@"mean-system-size-all"}] , 
Plot[Evaluate[y = MeanSystemSizeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}],  
AxesLabel->{"t","mean system size all"}]
Export[FileNameJoin@{outputDir,"MeanSystemSizeAll.pdf"},%];


(* ::Subsubsection::Closed:: *)
(*Mean Queue Time*)


Show[ListPlot[ {Load@"mean-queue-time-normal"}] ,  
AxesLabel->{"t","mean queue time tourist"}]
Export[FileNameJoin@{outputDir,"MeanQueueTime.pdf"},%];


Show[ListPlot[ {Load@"mean-queue-time-resident"}],
AxesLabel->{"t","mean queue time resident"}]
Export[FileNameJoin@{outputDir,"MeanQueueTimeResident.pdf"},%];


Show[ListPlot[ {Load@"mean-queue-time-all"},
PlotRange->All] , 
Plot[Evaluate[y = MeanQueueTimeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}],
AxesLabel->{"t","mean queue time all"}]
Export[FileNameJoin@{outputDir,"MeanQueueTimeAll.pdf"},%];


(* ::Subsubsection:: *)
(*Mean System Time*)


Show[ListPlot[ {Load@"mean-system-time-normal"}], 
AxesLabel->{"t","mean system time tourist"}]
Export[FileNameJoin@{outputDir,"MeanSystemTime.pdf"},%];


Show[ListPlot[ {Load@"mean-system-time-resident"}], 
AxesLabel->{"t","mean system time resident"}]
Export[FileNameJoin@{outputDir,"MeanSystemTimeResident.pdf"},%];


Show[ListPlot[ {Load@"mean-system-time-all"},
PlotRange->All] , 
Plot[Evaluate[y = MeanSystemTimeTheoretical], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
AxesLabel->{"t","mean system time all"}]
Export[FileNameJoin@{outputDir,"MeanSystemTimeAll.pdf"},%];


(* ::Section:: *)
(*Queue Size StepPlot*)


queueSizeNormal = Load@"queue-size-normal";
  ListStepPlot[queueSizeNormal,{ AxesLabel->{"t","queue tourist"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotNormalAll.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[queueSizeNormal[[;;100]],{ AxesLabel->{"t","queue toursit"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotNormalFirst.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[queueSizeNormal[[-100;;]],{ AxesLabel->{"t","queue tourist"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotNormalLast.pdf"},%];


queueSizeResident = Load@"queue-size-resident";
  ListStepPlot[queueSizeResident,{ AxesLabel->{"t","queue resident"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotResidentAll.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[queueSizeResident[[;;100]],{ AxesLabel->{"t","queue resident"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotResidentFirst.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[queueSizeResident[[-100;;]],{ AxesLabel->{"t","queue resident"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotResidentLast.pdf"},%];


queueSizeAll = Load@"queue-size-all";
  ListStepPlot[queueSizeAll,{ AxesLabel->{"t","queue all"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllAll.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[queueSizeAll[[;;100]],{ AxesLabel->{"t","queue all"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllFirst.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[queueSizeAll[[-100;;]],{ AxesLabel->{"t","queue all"}, Filling->Axis}]
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


ListStepPlot[dataSet,{ AxesLabel->{"t","queue tourist"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllFiltered.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[dataSet[[;;100]],{ AxesLabel->{"t","queue tourist"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotFirstFiltered.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[dataSet[[-100;;]],{ AxesLabel->{"t","queue tourist"}, Filling->Axis}]
    Export[FileNameJoin@{outputDir,"QueueStepPlotLastFiltered.pdf"},%];


dataSet = queueSizeResident;
(*Liste in verkehrter Reihenfolge sortieren*)
dataSet = Reverse[dataSet];
(*L\[ODoubleDot]sche alle Eintr\[ADoubleDot]ge mit der selben Zeit au\[SZ]er den ersten... Deswegen musste die Liste umgekehrt sortiert werden*)
dataSet = DeleteDuplicatesBy[dataSet, First];
(*Sortiere Liste wieder in die Urspr\[UDoubleDot]ngliche Form zur\[UDoubleDot]ck*)
dataSet = Reverse[dataSet];


ListStepPlot[dataSet,{ AxesLabel->{"t","queue resident"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotResidentFiltered.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[dataSet[[;;100]],{ AxesLabel->{"t","queue resident"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotResidentFirstFiltered.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[dataSet[[-100;;]],{ AxesLabel->{"t","queue resident"}, Filling->Axis}]
    Export[FileNameJoin@{outputDir,"QueueStepPlotResidentLastFiltered.pdf"},%];


dataSet = queueSizeAll;
(*Liste in verkehrter Reihenfolge sortieren*)
dataSet = Reverse[dataSet];
(*L\[ODoubleDot]sche alle Eintr\[ADoubleDot]ge mit der selben Zeit au\[SZ]er den ersten... Deswegen musste die Liste umgekehrt sortiert werden*)
dataSet = DeleteDuplicatesBy[dataSet, First];
(*Sortiere Liste wieder in die Urspr\[UDoubleDot]ngliche Form zur\[UDoubleDot]ck*)
dataSet = Reverse[dataSet];


ListStepPlot[dataSet,{ AxesLabel->{"t","queue all"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllFiltered.pdf"},%];
  (* startphase anfang leer!*)
  ListStepPlot[dataSet[[;;100]],{ AxesLabel->{"t","queue all"}, Filling->Axis}]
  Export[FileNameJoin@{outputDir,"QueueStepPlotAllFirstFiltered.pdf"},%];
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[dataSet[[-100;;]],{ AxesLabel->{"t","queue all"}, Filling->Axis}]
    Export[FileNameJoin@{outputDir,"QueueStepPlotAllLastFiltered.pdf"},%];


(* ::Section:: *)
(*Theorem von Little : lambda * MeanSystemTime - MeanSystemSize = 0 f\[UDoubleDot]r t-> inf*)


(* ::Text:: *)
(*Im eingeschwungenen Zustand muss der Wert 0 betragen*)


ListPlot[Load@"little-system-all", { AxesLabel->{"t","\!\(\*SubscriptBox[\(\[Lambda]M\), \(s\)]\) - \!\(\*SubscriptBox[\(L\), \(s\)]\)"}, Filling->Axis}]
Export[FileNameJoin@{outputDir,"LittleSystem.pdf"},%];


(* ::Section:: *)
(*Ausgabe der Ergebnisse in Tabelle*)


(* ::Text:: *)
(*Achtung: hier wird ienfach der letzte Wert der Simulation verwendet*)


TableOfResults = Grid[{
{"value", "theoretical", "simulation", },
{"mean queue size", N@MeanQueueSizeTheoretical, Last@Last[Load@"mean-queue-size-normal"]},
{"mean queue time", N@MeanQueueTimeTheoretical, Last@Last[Load@"mean-queue-time-normal"]},
{"mean system size", N@MeanSystemSizeTheoretical, Last@Last[Load@"mean-system-size-normal"]},
{"mean system time", N@MeanSystemTimeTheoretical, Last@Last[Load@"mean-system-time-normal"] }}]
Export[FileNameJoin@{outputDir,"ResultsTable.tex"},%, "TexFragment"];
