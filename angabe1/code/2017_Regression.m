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
l\[ADoubleDot]ngeTreppe = :9.39;


(*Nur die relevanten Spalten werden verwendet.*)
(*Die Geschwindigkeiten werden in m/s berechnet.*)
auf = dataGeschwindigkeiten[Select[#Treppe == "auf"&], <|
"id"->"ID Proband", 
"runde"->"Runde",
"bemerkung"-> "Bemerkung",
 "vAuf"->(l\[ADoubleDot]ngeTreppe/#"Zeit in sec"&)
|>];
ab = dataGeschwindigkeiten[Select[#Treppe == "ab"&], <|
"id"->"ID Proband", 
"runde"->"Runde",
"bemerkung"-> "Bemerkung",
 "vAb"->(l\[ADoubleDot]ngeTreppe/#"Zeit in sec"&)
|>];
ebene = dataGeschwindigkeiten[Select[#Ebene == "x"&], <|
"id"->"ID Proband", 
"runde"->"Runde",
"bemerkung"-> "Bemerkung",
 "vEbene"->(l\[ADoubleDot]ngeEbene/#"Zeit in sec"&)
|>];
(*Nur die Gr\[ODoubleDot]\[SZ]e der Probanden wird ben\[ODoubleDot]tigt.*)
gr\[ODoubleDot]\[SZ]e = dataProbanden[ All,<|
"id"->"ID Proband",
"gr\[ODoubleDot]\[SZ]e"->"Gr\[ODoubleDot]\[SZ]e in cm"
|>];

(*Die Daten werden zusammen in eine Tabelle gejoint. Dies stellt sicher, dass die einzelnen Werte passend zur ID des Probanden und der Runde zugeordnet werden.*)
abauf = JoinAcross[auf, ab, {"id","runde"}];
abaufebene = JoinAcross[abauf,dataEbene, {"id","runde"}];
data = JoinAcross[abaufebene, gr\[ODoubleDot]\[SZ]e,{"id"}];


(* ::Section:: *)
(*Treppengeschwindigkeit aufw\[ADoubleDot]rts*)


(*Herausgefilterte Werte: *)
data[Select[#bemerkung !=""&]]

data = data[Select[#bemerkung == ""&]];



(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Ebenengeschwindigkeit*)
d = Normal@Values@data[Select[#bemerkung==""&],{"vAuf","vEbene"}];
lm = LinearModelFit[d,{x},{x}]
Show[ListPlot[lm["Data"],PlotStyle->Orange],Plot[lm[x],{x,0,100}]]
lm["ANOVATable"]


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Gr\[ODoubleDot]\[SZ]e des Probanden*)
d = Normal@Values@data[Select[#bemerkung==""&],{"vAuf","gr\[ODoubleDot]\[SZ]e"}];
lm = LinearModelFit[d,{x},{x}]
Show[ListPlot[lm["Data"],PlotStyle->Orange],Plot[lm[x],{x,0,100}]]
lm["ANOVATable"]


(*Modell f\[UDoubleDot]r Abh\[ADoubleDot]ngigkeit von der Runde*)
d = Normal@Values@data[Select[#bemerkung==""&],{"vAuf","runde"}];
lm = LinearModelFit[d,{x},{x}]
Show[ListPlot[lm["Data"],PlotStyle->Orange],Plot[lm[x],{x,0,100}]]
lm["ANOVATable"]



