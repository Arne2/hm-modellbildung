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



(* umso n\[ADoubleDot]her a an c umso gr\[ODoubleDot]\[SZ]er skip probieren *)
Simulate["-a 1000 -c 100 -d 1000000 -vip 0 -conf 1 -skip 100"]

"Plots gefordert aus angabe:"
{
  ListPlot[Load@"mean-queue-size-normal",PlotRange->All],
  ListPlot[Load@"mean-system-size-normal",PlotRange->All],
  ListPlot[Load@"mean-queue-time-normal",PlotRange->All],
  ListPlot[Load@"mean-system-time-normal",PlotRange->All],
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
  ListPlot[Load@"little-system-normal",PlotRange->All]
}



Simulate["-a 1000 -c 100 -d 10000000 -vip 50 -conf 2 -skip 1000"]

"Plots f\[UDoubleDot]r ausschlie\[SZ]lich VIPS: (passt glaub noch nicht so ganz, vllt logik falsch)"
{
  ListPlot[Load@"mean-queue-size-resident",PlotRange->All],
  ListPlot[Load@"mean-system-size-resident",PlotRange->All],
  ListPlot[Load@"mean-queue-time-resident",PlotRange->All],
  ListPlot[Load@"mean-system-time-resident",PlotRange->All],
  ListPlot[Load@"mean-phone-size-resident",PlotRange->All]
}

"Plot Anzahl in Queue"
queueSize = Load@"queue-size-resident";
{
  ListStepPlot[queueSize,Filling->Bottom],
  (* startphase anfang leer!*)
  ListStepPlot[queueSize[[;;100]],Filling->Bottom],
  (* ende (wo es l\[ADoubleDot]uft) *)
  ListStepPlot[queueSize[[-100;;]],Filling->Bottom]
}

"Plots little"
{
  ListPlot[Load@"little-queue-resident",PlotRange->All],
  ListPlot[Load@"little-system-resident",PlotRange->All]
}



