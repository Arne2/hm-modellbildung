(* ::Package:: *)

(* ::Title:: *)
(*Lineare Regression der Daten von 2017*)


ohneAusreisser = 1;

outputDir = If[ohneAusreisser > 0, 
    FileNameJoin@{NotebookDirectory[], "../abbildungen/regression/2017/ohneausreisser/"},
	FileNameJoin@{NotebookDirectory[], "../abbildungen/regression/2017/"}
];


(* ::Section::Closed:: *)
(*Aufbereitung der Daten*)


(*Hier werden die Daten aus dem Messexperiment geladen.*)
pathGeschwindigkeiten= "../data/2017/MessexperimentGeschwindigkeiten.csv";
dataGeschwindigkeiten= SemanticImport[FileNameJoin@{NotebookDirectory[],pathGeschwindigkeiten}, CharacterEncoding->"UTF8"];
pathProbanden= "../data/2017/MessexperimentProbanden.csv";
dataProbanden= SemanticImport[FileNameJoin@{NotebookDirectory[],pathProbanden},CharacterEncoding->"UTF8"];


(*Konstanten f\[UDoubleDot]r die L\[ADoubleDot]nge der Ebene und der Treppe in Meter.Die Treppenstrecke wurde projeziert auf die Horizontale.*)
l\[ADoubleDot]ngeEbene = 27.3;
l\[ADoubleDot]ngeTreppe = 9.39;


(*Nur die relevanten Spalten werden verwendet.*)
(*Die Geschwindigkeiten werden in m/s berechnet.*)
auf = dataGeschwindigkeiten[Select[#Treppe == "auf"&], <|
"id"->"ID Proband", 
"runde"->"Runde",
"bemerkungAuf"-> "Bemerkung",
 "vAuf"->(l\[ADoubleDot]ngeTreppe/#"Zeit in sec"&)
|>];
ab = dataGeschwindigkeiten[Select[#Treppe == "ab"&], <|
"id"->"ID Proband", 
"runde"->"Runde",
"bemerkungAb"-> "Bemerkung",
 "vAb"->(l\[ADoubleDot]ngeTreppe/#"Zeit in sec"&)
|>];
ebene = dataGeschwindigkeiten[Select[#Ebene == "x"&], <|
"id"->"ID Proband", 
"runde"->"Runde",
"bemerkungEbene"-> "Bemerkung",
 "vEbene"->(l\[ADoubleDot]ngeEbene/#"Zeit in sec"&)
|>];
(*Nur die Gr\[ODoubleDot]\[SZ]e der Probanden wird ben\[ODoubleDot]tigt.*)
gr\[ODoubleDot]\[SZ]eData = dataProbanden[ All,<|
"id"->"ID Proband",
"gr\[ODoubleDot]\[SZ]e"->"Groesse in cm"
|>];

(*Die Daten werden zusammen in eine Tabelle gejoint. Dies stellt sicher, dass die einzelnen Werte passend zur ID des Probanden und der Runde zugeordnet werden.*)
abauf = JoinAcross[auf, ab, {"id","runde"}];
abaufebene = JoinAcross[abauf,ebene, {"id","runde"}];
data = JoinAcross[abaufebene, gr\[ODoubleDot]\[SZ]eData,{"id"}];


(* ::Section::Closed:: *)
(*Treppengeschwindigkeit aufw\[ADoubleDot]rts*)


(*Herausgefilterte Werte: *)
a = TableForm[
	Normal@Values@data[Select[#bemerkungAuf !=""&], {"id","runde", "bemerkungAuf", "vAuf"}] ,
	TableHeadings->{None,{"ID Proband", "Runde", "Bemerkung", "v Treppe aufw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\)"}}
]
Export[FileNameJoin@{outputDir,"auf-ausreisser.tex"},a, "TexFragment"];

dataAuf = If[ohneAusreisser > 0, 
	data[Select[#bemerkungAuf == "" &]],
	data
];
dataAufAusreisser = If[ohneAusreisser > 0, 
	data[Select[#bemerkungAuf != "" &]],
	data[Select[0==1&]]
];



(* ::Subsubsection::Closed:: *)
(*Lineare Regression mit einem Parameter *)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Ebenengeschwindigkeit*)
d = Normal@Values@dataAuf[All,{"vEbene","vAuf"}];
dA = If[ohneAusreisser > 0, 
	Normal@Values@dataAufAusreisser[All,{"vEbene","vAuf"}],
	\[EmptySet]
];

lm = LinearModelFit[d,{vEbene},{vEbene}]
Export[FileNameJoin@{outputDir,"auf-ebene.tex"},Normal@%, "TexFragment"];
Show[
	ListPlot[{d,dA},PlotStyle->{Orange, Black}, AxesLabel->{"v Ebene in \!\(\*FractionBox[\(m\), \(s\)]\)","v Aufw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "}, PlotRange->All],
	Plot[lm[x],{x,0,100}]
]
Export[FileNameJoin@{outputDir,"auf-ebene.pdf"},%];

lm["ParameterTable"]
Export[FileNameJoin@{outputDir,"auf-ebene-table.tex"},%, "TexFragment"];


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Gr\[ODoubleDot]\[SZ]e des Probanden*)
d = Normal@Values@dataAuf[All,{"gr\[ODoubleDot]\[SZ]e","vAuf"}];
dA = If[ohneAusreisser > 0, 
	Normal@Values@dataAufAusreisser[All,{"gr\[ODoubleDot]\[SZ]e","vAuf"}],
	\[EmptySet]
];
lm = LinearModelFit[d,{gr\[ODoubleDot]\[SZ]e},{gr\[ODoubleDot]\[SZ]e}]
Export[FileNameJoin@{outputDir,"auf-groesse.tex"},Normal@%, "TexFragment"];
	
Show[
ListPlot[{d,dA},PlotStyle->{Orange,Black}, AxesLabel->{"Gr\[ODoubleDot]\[SZ]e in cm","v Aufw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "},PlotRange->All],
Plot[lm[x],{x, 150,250}]
]
Export[FileNameJoin@{outputDir,"auf-groesse.pdf"},%];

lm["ParameterTable"]
Export[FileNameJoin@{outputDir,"auf-groesse-table.tex"},%, "TexFragment"];


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Runde*)
d = Normal@Values@dataAuf[All,{"runde","vAuf"}];
dA = If[ohneAusreisser > 0, 
	Normal@Values@dataAufAusreisser[All,{"runde","vAuf"}],
	\[EmptySet]
];
lm = LinearModelFit[d,{runde},{runde}]
Export[FileNameJoin@{outputDir,"auf-runde.tex"},Normal@%, "TexFragment"];
Show[
	ListPlot[{d,dA},PlotStyle->{Orange,Black}, AxesLabel->{"Runde","v Aufw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "},PlotRange->All],
	Plot[lm[x],{x, 0,100}]
]
Export[FileNameJoin@{outputDir,"auf-runde.pdf"},%];

lm["ParameterTable"]
Export[FileNameJoin@{outputDir,"auf-runde-table.tex"},%, "TexFragment"];


(* ::Subsubsection:: *)
(*Lineare Regressions mit zwei Parameter*)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von Wunschgeschwindigkeit (Ebene) und K\[ODoubleDot]rpergr\[ODoubleDot]\[SZ]e*)
d = Normal@Values@dataAuf[All,{"vEbene","gr\[ODoubleDot]\[SZ]e","vAuf"}];
dA = If[ohneAusreisser > 0, 
	Normal@Values@dataAufAusreisser[All,{"vEbene","gr\[ODoubleDot]\[SZ]e","vAuf"}],
	\[EmptySet]
];
lm = LinearModelFit[d,{vEbene,gr\[ODoubleDot]\[SZ]e},{vEbene,gr\[ODoubleDot]\[SZ]e}]
Export[FileNameJoin@{outputDir,"auf-ebene-groesse.tex"},Normal@%, "TexFragment"];
Show [
	ListPointPlot3D[If[ohneAusreisser > 0, {d,dA},d],PlotStyle->{Orange,Black,Orange},PlotStyle->PointSize[0.01],PlotRange->All],
	Plot3D[{Normal@lm},{vEbene,0,3},{gr\[ODoubleDot]\[SZ]e,150,200}, PlotStyle->Directive[Opacity@0.2, Blue]]
]
Export[FileNameJoin@{outputDir,"auf-ebene-groesse.pdf"},%];

lm["ParameterTable"]
Export[FileNameJoin@{outputDir,"auf-ebene-groesse-table.tex"},%, "TexFragment"];


(* ::Subsubsection:: *)
(*Lineare Regression mit drei Variablen*)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von Rundennummer, Wunschgeschwindigkeit (Ebene) und K\[ODoubleDot]rpergr\[ODoubleDot]\[SZ]e*)
d = Normal@Values@dataAuf[All,{"runde","vEbene","gr\[ODoubleDot]\[SZ]e","vAuf"}];
lm = LinearModelFit[d,{runde,vEbene,gr\[ODoubleDot]\[SZ]e},{runde,vEbene,gr\[ODoubleDot]\[SZ]e}]
Export[FileNameJoin@{outputDir,"auf-runde-ebene-groesse.tex"},Normal@%, "TexFragment"];
lm["ParameterTable"]
Export[FileNameJoin@{outputDir,"auf-runde-ebene-groesse-table.tex"},%, "TexFragment"];


(* ::Section:: *)
(*Treppengeschwindigkeit abw\[ADoubleDot]rts*)


(*Herausgefilterte Werte: *)
a = TableForm[
	Normal@Values@data[Select[#bemerkungAb !=""&], {"id","runde", "bemerkungAb", "vAb"}] ,
	TableHeadings->{None,{"ID Proband", "Runde", "Bemerkung", "v Treppe abw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "}}
]
Export[FileNameJoin@{outputDir,"ab-ausreisser.tex"},a, "TexFragment"];

dataAb = If[ohneAusreisser > 0, 
	data[Select[#bemerkungAb == ""&]],
	data
];
dataAbAusreisser = If[ohneAusreisser > 0, 
	data[Select[#bemerkungAb!= "" &]],
	data[Select[0 == 1 &]]
];


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Ebenengeschwindigkeit*)
d = Normal@Values@dataAb[All,{"vEbene","vAb"}];
dA = If[ohneAusreisser > 0, 
	Normal@Values@dataAbAusreisser[All,{"vEbene","vAb"}],
	\[EmptySet]
];
lm = LinearModelFit[d,{vEbene},{vEbene}]
Export[FileNameJoin@{outputDir,"ab-ebene.tex"},Normal@%,"TeXFragment"];
Show[
	ListPlot[{d,dA},PlotStyle->{Orange,Black}, AxesLabel->{"v Ebene in \!\(\*FractionBox[\(m\), \(s\)]\)","v Abw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "},PlotRange->All],
	Plot[lm[x],{x,0,100}]
]
Export[FileNameJoin@{outputDir,"ab-ebene.pdf"},%];

lm["ParameterTable"]
Export[FileNameJoin@{outputDir,"ab-ebene-table.tex"},%,"TeXFragment"];


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Gr\[ODoubleDot]\[SZ]e des Probanden*)
d = Normal@Values@dataAb[All,{"gr\[ODoubleDot]\[SZ]e","vAb"}];
dA = If[ohneAusreisser > 0, 
	Normal@Values@dataAbAusreisser[All,{"gr\[ODoubleDot]\[SZ]e","vAb"}],
	\[EmptySet]
];
lm = LinearModelFit[d,{gr\[ODoubleDot]\[SZ]e},{gr\[ODoubleDot]\[SZ]e}]
Export[FileNameJoin@{outputDir,"ab-groesse.tex"},Normal@%,"TeXFragment"];
Show[
	ListPlot[{d,dA},PlotStyle->{Orange,Black}, AxesLabel->{"Gr\[ODoubleDot]\[SZ]e in cm","v Abw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "},PlotRange->All],
	Plot[lm[x],{x,150,200}]
]
Export[FileNameJoin@{outputDir,"ab-groesse.pdf"},%];
lm["ParameterTable"]
Export[FileNameJoin@{outputDir,"ab-groesse-table.tex"},%,"TeXFragment"];


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Runde*)
d = Normal@Values@dataAb[All,{"runde","vAb"}];
dA = If[ohneAusreisser > 0, 
	Normal@Values@dataAbAusreisser[All,{"runde","vAb"}],
	\[EmptySet]
];
lm = LinearModelFit[d,{runde},{runde}]
Export[FileNameJoin@{outputDir,"ab-runde.tex"},Normal@%,"TeXFragment"];
Show[
	ListPlot[{d,dA},PlotStyle->{Orange,Black}, AxesLabel->{"Runde","v Abw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "},PlotRange->All],
	Plot[lm[x],{x,0,100}]
]
Export[FileNameJoin@{outputDir,"ab-runde.pdf"},%];
lm["ParameterTable"]
Export[FileNameJoin@{outputDir,"ab-runde-table.tex"},%,"TeXFragment"];


(* ::Subsubsection:: *)
(*Lineare Regressions mit zwei Parameter*)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von Wunschgeschwindigkeit (Ebene) und K\[ODoubleDot]rpergr\[ODoubleDot]\[SZ]e*)
d = Normal@Values@dataAb[All,{"vEbene","gr\[ODoubleDot]\[SZ]e","vAb"}];
dA = If[ohneAusreisser > 0, 
	Normal@Values@dataAbAusreisser[All,{"vEbene","gr\[ODoubleDot]\[SZ]e","vAb"}],
	\[EmptySet]
];
lm = LinearModelFit[d,{vEbene,gr\[ODoubleDot]\[SZ]e},{vEbene,gr\[ODoubleDot]\[SZ]e}]
Export[FileNameJoin@{outputDir,"ab-ebene-groesse.tex"},Normal@%,"TeXFragment"];
Show [
	ListPointPlot3D[If[ohneAusreisser > 0,{d,dA},d],PlotStyle->{Orange,Black, Orange},PlotStyle->PointSize[0.01],PlotRange->All],
	Plot3D[{Normal@lm},{vEbene,0,3},{gr\[ODoubleDot]\[SZ]e,150,200},PlotStyle->Directive[Opacity@0.2, Blue]]
]
Export[FileNameJoin@{outputDir,"ab-ebene-groesse.pdf"},%];

lm["ParameterTable"]
Export[FileNameJoin@{outputDir,"ab-ebene-groesse-table.tex"},%,"TeXFragment"];


(* ::Subsubsection:: *)
(*Lineare Regression mit drei Variablen*)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von Rundennummer, Wunschgeschwindigkeit (Ebene) und K\[ODoubleDot]rpergr\[ODoubleDot]\[SZ]e*)
d = Normal@Values@dataAuf[All,{"runde","vEbene","gr\[ODoubleDot]\[SZ]e","vAb"}];
lm = LinearModelFit[d,{runde,vEbene,gr\[ODoubleDot]\[SZ]e},{runde,vEbene,gr\[ODoubleDot]\[SZ]e}]
Export[FileNameJoin@{outputDir,"ab-runde-ebene-groesse.tex"},Normal@%,"TeXFragment"];
lm["ParameterTable"]
Export[FileNameJoin@{outputDir,"ab-runde-ebene-groesse-table.tex"},%,"TeXFragment"];









