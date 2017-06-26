(* ::Package:: *)

outputDir = FileNameJoin@{NotebookDirectory[], "../doku/abbildungen/"};
path = "fast-marching.xmlperson_velocity.csv";
data = SemanticImport[FileNameJoin@{NotebookDirectory[],path}, CharacterEncoding->"UTF8"];


velocities = data[Select[#id != ""&], <|
(*"velocity"->"velocity"*)
 "velocity"->(N[Round[#"velocity",10^-2]]&)
|>];

geschwMean = Mean[velocities[All, "velocity"]]
geschwDeviation = StandardDeviation[velocities[All, "velocity"]]

(*Show[Histogram[Transpose@velocities, {.01}],AxesLabel->{HoldForm[t[s]],HoldForm[rel.H\[ADoubleDot]ufigkeit]}]*)
(*Export[FileNameJoin@{outputDir,"histogramm.pdf"},%];*)


Show[Histogram[velocities[All, "velocity"],100, "PDF"],Plot[PDF[NormalDistribution[geschwMean,geschwDeviation],x],{x,1.065,1.935},PlotStyle->Thick],AxesLabel->{HoldForm[v[m/s]],HoldForm[rel.H\[ADoubleDot]ufigkeit]}]
Export[FileNameJoin@{outputDir,"histogramm.pdf"},%];
