(* ::Package:: *)

SetDirectory[FileNameJoin@{NotebookDirectory[], "/"}]
outputDir =  FileNameJoin@{NotebookDirectory[], "/"};
Load[name_]:= Import[FileNameJoin@{NotebookDirectory[], name <> ".csv"},HeaderLines->1];



euclid = Import[FileNameJoin@{NotebookDirectory[], "euclid" <> ".csv"},HeaderLines->1];
fastmarching = Import[FileNameJoin@{NotebookDirectory[], "fastmarching" <> ".csv"},HeaderLines->1];
error = Import[FileNameJoin@{NotebookDirectory[], "error" <> ".csv"},HeaderLines->1];




ListPlot3D[error[1], error[2], error[3]]



(* ::Code::Initialization::Bold:: *)
ListPlot3D[{error[All, 1], error[All, 2], error[All, 3]}]


ListPlot3D@euclid
Export[FileNameJoin@{outputDir,"Euklid.pdf"},%];
ListPlot3D@fastmarching
Export[FileNameJoin@{outputDir,"FastMarching.pdf"},%];
ListPlot3D@error
Export[FileNameJoin@{outputDir,"Error.pdf"},%];



