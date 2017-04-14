(* ::Package:: *)

(* ::Title:: *)
(*Lineare Regression der Daten von 2017*)


(* ::Section::Closed:: *)
(*Aufbereitung der Daten*)


(*Hier werden die Daten aus dem Messexperiment geladen.*)
pathGeschwindigkeiten= "../data/2017/MessexperimentGeschwindigkeitenSS2017.csv";
dataGeschwindigkeiten= SemanticImport@FileNameJoin@{NotebookDirectory[],pathGeschwindigkeiten};
pathProbanden= "../data/2017/MessexperimentProbanden.csv";
dataProbanden= SemanticImport@FileNameJoin@{NotebookDirectory[],pathProbanden};


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
gr\[ODoubleDot]\[SZ]e = dataProbanden[ All,<|
"id"->"ID Proband",
"gr\[ODoubleDot]\[SZ]e"->"Gr\[ODoubleDot]\[SZ]e in cm"
|>];

(*Die Daten werden zusammen in eine Tabelle gejoint. Dies stellt sicher, dass die einzelnen Werte passend zur ID des Probanden und der Runde zugeordnet werden.*)
abauf = JoinAcross[auf, ab, {"id","runde"}];
abaufebene = JoinAcross[abauf,ebene, {"id","runde"}];
data = JoinAcross[abaufebene, gr\[ODoubleDot]\[SZ]e,{"id"}];


(* ::Section:: *)
(*Treppengeschwindigkeit aufw\[ADoubleDot]rts*)


(*Herausgefilterte Werte: *)
data[Select[#bemerkungAuf !=""&]]
dataAuf = data[Select[#bemerkungAuf == ""&]];
(*dataAuf=data;*) (*Ausrei\[SZ]er mit ber\[UDoubleDot]cksichtigen*)


(* ::Subsubsection:: *)
(*Lineare Regression mit einem Parameter *)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Ebenengeschwindigkeit*)
d = Normal@Values@dataAuf[All,{"vEbene","vAuf"}];
lm = LinearModelFit[d,{x},{x}]
Show[
	ListPlot[lm["Data"],PlotStyle->Orange, AxesLabel->{"v Ebene in \!\(\*FractionBox[\(m\), \(s\)]\)","v Aufw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "}],
	Plot[lm[x],{x,0,100}]
]
lm["ParameterTable"]


(* ::Subsubsection:: *)
(*Konditionierung*)


A = lm["DesignMatrix"];
R = QRDecomposition[A][[2]];
cond1 = LinearAlgebra`MatrixConditionNumber[Transpose[A].A]
Log[cond1]
cond2 = LinearAlgebra`MatrixConditionNumber[R]
Log[cond2]


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Gr\[ODoubleDot]\[SZ]e des Probanden*)
d = Normal@Values@dataAuf[All,{"gr\[ODoubleDot]\[SZ]e","vAuf"}];
lm = LinearModelFit[d,{x},{x}]
Show[
	ListPlot[lm["Data"],PlotStyle->Orange, AxesLabel->{"Gr\[ODoubleDot]\[SZ]e in cm","v Aufw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "}],
	Plot[lm[x],{x, 150,200}]
]
lm["ParameterTable"]


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Runde*)
d = Normal@Values@dataAuf[All,{"runde","vAuf"}];
lm = LinearModelFit[d,{x},{x}]
Show[
	ListPlot[lm["Data"],PlotStyle->Orange, AxesLabel->{"Runde","v Aufw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "}],
	Plot[lm[x],{x, 0,100}]
]
lm["ParameterTable"]


(* ::Subsubsection:: *)
(*Lineare Regressions mit zwei Parameter*)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von Wunschgeschwindigkeit (Ebene) und K\[ODoubleDot]rpergr\[ODoubleDot]\[SZ]e*)
d = Normal@Values@dataAuf[All,{"vEbene","gr\[ODoubleDot]\[SZ]e","vAuf"}];
lm = LinearModelFit[d,{x,y},{x,y}]
Show [
	ListPointPlot3D[d,PlotStyle->PointSize[0.01]],
	Plot3D[{Normal@lm},{x,0,3},{y,150,200}]
	]
lm["ParameterTable"]



(* ::Subsubsection:: *)
(*Lineare Regression mit drei Variablen*)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von Rundennummer, Wunschgeschwindigkeit (Ebene) und K\[ODoubleDot]rpergr\[ODoubleDot]\[SZ]e*)
d = Normal@Values@dataAuf[All,{"runde","vEbene","gr\[ODoubleDot]\[SZ]e","vAuf"}];
lm = LinearModelFit[d,{x,y,z},{x,y,z}]
lm["ParameterTable"]


(* ::Section:: *)
(*Treppengeschwindigkeit abw\[ADoubleDot]rts*)


(*Herausgefilterte Werte*)
data[Select[#bemerkungAb != ""&]]
dataAb = data[Select[#bemerkungAb == ""&]];
(*dataAb = data;*) (*Ausrei\[SZ]er mit ber\[UDoubleDot]cksichtigen*)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Ebenengeschwindigkeit*)
d = Normal@Values@dataAb[All,{"vEbene","vAb"}];
lm = LinearModelFit[d,{x},{x}]
Show[
	ListPlot[lm["Data"],PlotStyle->Orange, AxesLabel->{"v Ebene in \!\(\*FractionBox[\(m\), \(s\)]\)","v Abw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "}],
	Plot[lm[x],{x,0,100}]]
lm["ParameterTable"]


(* ::Subsubsection:: *)
(*Konditionierung*)


A = lm["DesignMatrix"];
R = QRDecomposition[A][[2]];
cond1 = LinearAlgebra`MatrixConditionNumber[Transpose[A].A]
Log[cond1]
cond2 = LinearAlgebra`MatrixConditionNumber[R]
Log[cond2]


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Gr\[ODoubleDot]\[SZ]e des Probanden*)
d = Normal@Values@dataAb[All,{"gr\[ODoubleDot]\[SZ]e","vAb"}];
lm = LinearModelFit[d,{x},{x}]
Show[
	ListPlot[lm["Data"],PlotStyle->Orange, AxesLabel->{"Gr\[ODoubleDot]\[SZ]e in cm","v Abw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "}],
	Plot[lm[x],{x,150,200}]]
lm["ParameterTable"]


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Runde*)
d = Normal@Values@dataAb[All,{"runde","vAb"}];
lm = LinearModelFit[d,{x},{x}]
Show[
	ListPlot[lm["Data"],PlotStyle->Orange, AxesLabel->{"Runde","v Abw\[ADoubleDot]rts in \!\(\*FractionBox[\(m\), \(s\)]\) "}],
	Plot[lm[x],{x,0,100}]]
lm["ParameterTable"]








(* ::Subsubsection:: *)
(*Lineare Regressions mit zwei Parameter*)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von Wunschgeschwindigkeit (Ebene) und K\[ODoubleDot]rpergr\[ODoubleDot]\[SZ]e*)
d = Normal@Values@dataAuf[All,{"vEbene","gr\[ODoubleDot]\[SZ]e","vAb"}];
lm = LinearModelFit[d,{x,y},{x,y}]
Show [
	ListPointPlot3D[d,PlotStyle->PointSize[0.01]],
	Plot3D[{Normal@lm},{x,0,3},{y,150,200}]
	]
lm["ParameterTable"]



(* ::Subsubsection:: *)
(*Konditionierung*)


A = lm["DesignMatrix"];
R = QRDecomposition[A][[2]];
cond1 = LinearAlgebra`MatrixConditionNumber[Transpose[A].A]
Log[cond1]
cond2 = LinearAlgebra`MatrixConditionNumber[R]
Log[cond2]


(* ::Subsubsection:: *)
(*Lineare Regression mit drei Variablen*)


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von Rundennummer, Wunschgeschwindigkeit (Ebene) und K\[ODoubleDot]rpergr\[ODoubleDot]\[SZ]e*)
d = Normal@Values@dataAuf[All,{"runde","vEbene","gr\[ODoubleDot]\[SZ]e","vAb"}];
lm = LinearModelFit[d,{x,y,z},{x,y,z}]
lm["ParameterTable"]









