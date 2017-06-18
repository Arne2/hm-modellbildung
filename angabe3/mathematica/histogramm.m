(* ::Package:: *)

path = "fast-marching.xmlperson_velocity.csv";
data = SemanticImport[FileNameJoin@{NotebookDirectory[],path}, CharacterEncoding->"UTF8"];


velocities = data[Select[#id != ""&], <|
(*"velocity"->"velocity"*)
 "velocity"->(N[Round[#"velocity",10^-2]]&)
|>]
Show[Histogram[velocities, {.01}],AxesLabel->{HoldForm[t[s]],HoldForm[rel.H\[ADoubleDot]ufigkeit]}]



