(* ::Package:: *)

SetDirectory[FileNameJoin@{NotebookDirectory[], "../code"}]
If[Run["mvn package"] == 1, "mvn nicht istalliert in Intellij maven target package ausf\[UDoubleDot]hren "]
Simulate[args_] := If[
	Run["java -jar " <> FileNameJoin@{NotebookDirectory[], "../code/target","queue-1.0-SNAPSHOT.jar "} <> args] == 1,
	"konnte simulation nicht ausf\[UDoubleDot]hren",
	"Simulation beendet"
]


NormalizeData[data_] := 


(* ::Section:: *)
(*Parameter f\[UDoubleDot]r Simulation*)


(*Falls neue Simulation ansteuern wert auf true setzen *)
(*Falls wert auf false, dann wird keine neue Simulation durchgef\[UDoubleDot]hrt, sondern alte Daten herangezoogen *)
outputDir =  FileNameJoin@{
NotebookDirectory[], StringJoin["../doku/abbildungen/2_Phone_VIP/Auslastung_Vegleich", 
"/"]};
(*Wirft ein Fehler falls directory bereits vorhanden... ist aber OK so *)
CreateDirectory[outputDir, CreateIntermediateDirectories -> True]
(*DatenPfad erzeugt sich automatisch aus den Paramtern*)
skip = 0;
durationSimulation = 1000000
dataPath1500 = StringJoin["../data/2_Phone_VIP_10_Arrival_", ToString@1500, "_Skip_", ToString@skip];
dataPath1000 = StringJoin["../data/2_Phone_VIP_10_Arrival_", ToString@1000, "_Skip_", ToString@skip];
dataPath800 = StringJoin["../data/2_Phone_VIP_10_Arrival_", ToString@800, "_Skip_", ToString@skip];
dataPath400 = StringJoin["../data/2_Phone_VIP_10_Arrival_", ToString@400, "_Skip_", ToString@skip];
dataPath100 = StringJoin["../data/2_Phone_VIP_10_Arrival_", ToString@100, "_Skip_", ToString@skip];
Load1500[name_]:= Import[FileNameJoin@{NotebookDirectory[],dataPath1500, name <> ".csv"},HeaderLines->1];
Load1000[name_]:= Import[FileNameJoin@{NotebookDirectory[],dataPath1000, name <> ".csv"},HeaderLines->1];
Load800[name_]:= Import[FileNameJoin@{NotebookDirectory[],dataPath800, name <> ".csv"},HeaderLines->1];
Load400[name_]:= Import[FileNameJoin@{NotebookDirectory[],dataPath400, name <> ".csv"},HeaderLines->1];
Load100[name_]:= Import[FileNameJoin@{NotebookDirectory[],dataPath100, name <> ".csv"},HeaderLines->1];



(* ::Section:: *)
(*Serverauslastung*)


data1500 = Load1500@"mean-phone-size-all";
data1500[[All,2]] = data1500[[All,2]]/2;
data1000 = Load1000@"mean-phone-size-all";
data1000[[All,2]] = data1500[[All,2]]/2;
data800 = Load800@"mean-phone-size-all";
data800[[All,2]] = data1500[[All,2]]/2;
data400 = Load400@"mean-phone-size-all";
data400[[All,2]] = data1500[[All,2]]/2;
data100 = Load100@"mean-phone-size-all";
data100[[All,2]] = data1500[[All,2]]/2;





Show[ListPlot[ data1500,
{PlotRange->All, PlotStyle -> {Blue, Dashed, Thick}}], 
ListPlot[data1000,
{PlotRange->All, PlotStyle -> {Purple, Dashed, Thick}}], 
ListPlot[data800,
{PlotRange->All, PlotStyle -> {Green, Dashed, Thick}}], 
ListPlot[data400,
{PlotRange->All, PlotStyle -> {Yellow, Dashed, Thick}}], 
ListPlot[data100,
{PlotRange->All, PlotStyle -> {Magenta, Dashed, Thick}}], 
Plot[Evaluate[y = 1], 
{x, 0, durationSimulation},
PlotStyle -> {Orange, Dashed, Thick}], 
PlotRange -> Automatic,
AxesLabel->{"t","mean phone workload"},
 (* The legends in the Epilog are starting here *)
 Epilog -> Inset[Column[{ 
      PointLegend[{Blue}, {" Arrival 1500"}],
      PointLegend[{Purple}, {" Arrival 1000"}],
      PointLegend[{Green}, {" Arrival 800"}],
      PointLegend[{Yellow}, {" Arrival 400"}],
      PointLegend[{Magenta}, {" Arrival 100"}]
      }], Scaled[{0.8, 0.65}]]
 (* End of the Legends *)]
Export[FileNameJoin@{outputDir,"MeanPhoneWorkload.pdf"},%];
