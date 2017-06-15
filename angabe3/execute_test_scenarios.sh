#!/bin/bash
cd /home/user/Documents/AAA_Studium/Master_1/Modellbildung_Simulation/Gruppe/hm-modellbildung/angabe3/code/target

echo "chickentest"
echo "euclid"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm euclid --field-image "../../test_scenarios/chickentest/map.png" --output "../../test_scenarios/chickentest/" --max-duration 50

echo "dijkstra"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm dijkstra --field-image "../../test_scenarios/chickentest/map.png" --output "../../test_scenarios/chickentest/"

echo "fast marching"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm fast-marching --field-image "../../test_scenarios/chickentest/map.png" --output "../../test_scenarios/chickentest/"


echo "==========================================="

echo "evacuation_2_doors_test_1"
echo "euclid"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm euclid --field-image "../../test_scenarios/evacuation_2_doors_test_1/map.png" --output "../../test_scenarios/evacuation_2_doors_test_1/" --max-duration 50

echo "dijkstra"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm dijkstra --field-image "../../test_scenarios/evacuation_2_doors_test_1/map.png" --output "../../test_scenarios/evacuation_2_doors_test_1/"

echo "fast marching"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm fast-marching --field-image "../../test_scenarios/evacuation_2_doors_test_1/map.png" --output "../../test_scenarios/evacuation_2_doors_test_1/"


echo "==========================================="

echo "evacuation_2_doors_test_2"
echo "euclid"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm euclid --field-image "../../test_scenarios/evacuation_2_doors_test_2/map.png" --output "../../test_scenarios/evacuation_2_doors_test_2/" --max-duration 50


echo "dijkstra"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm dijkstra --field-image "../../test_scenarios/evacuation_2_doors_test_2/map.png" --output "../../test_scenarios/evacuation_2_doors_test_2/"


echo "fast marching"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm fast-marching --field-image "../../test_scenarios/evacuation_2_doors_test_2/map.png" --output "../../test_scenarios/evacuation_2_doors_test_2/"

echo "==========================================="

echo "evacuation_2_doors_test_3"
echo "euclid"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm euclid --field-image "../../test_scenarios/evacuation_2_doors_test_3/map.png" --output "../../test_scenarios/evacuation_2_doors_test_3/" --max-duration 50


echo "dijkstra"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm dijkstra --field-image "../../test_scenarios/evacuation_2_doors_test_3/map.png" --output "../../test_scenarios/evacuation_2_doors_test_3/"


echo "fast marching"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm fast-marching --field-image "../../test_scenarios/evacuation_2_doors_test_3/map.png" --output "../../test_scenarios/evacuation_2_doors_test_3/"


echo "==========================================="

echo "evacuation_4_doors"
echo "euclid"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm euclid --field-image "../../test_scenarios/evacuation_4_doors/map.png" --output "../../test_scenarios/evacuation_4_doors/" --max-duration 50


echo "dijkstra"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm dijkstra --field-image "../../test_scenarios/evacuation_4_doors/map.png" --output "../../test_scenarios/evacuation_4_doors/"


echo "fast marching"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm fast-marching --field-image "../../test_scenarios/evacuation_4_doors/map.png" --output "../../test_scenarios/evacuation_4_doors/"


echo "==========================================="

echo "rimea"
echo "euclid"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm euclid --field-image "../../test_scenarios/rimea/map.png" --output "../../test_scenarios/rimea/" --max-duration 50


echo "dijkstra"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm dijkstra --field-image "../../test_scenarios/rimea/map.png" --output "../../test_scenarios/rimea/"


echo "fast marching"
java -jar angabe3-1.0-SNAPSHOT.jar --algorithm fast-marching --field-image "../../test_scenarios/rimea/map.png" --output "../../test_scenarios/rimea/"



