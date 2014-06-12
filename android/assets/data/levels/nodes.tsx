<?xml version="1.0" encoding="UTF-8"?>
<tileset name="nodes" tilewidth="32" tileheight="32">
 <image source="nodes.png" width="512" height="512"/>
 <tile id="0">
  <properties>
   <property name="playerstart" value=""/>
  </properties>
 </tile>
 <tile id="1">
  <properties>
   <property name="destroyable" value=""/>
   <property name="powercable" value=""/>
   <property name="triggervalue" value="1"/>
  </properties>
 </tile>
 <tile id="2">
  <properties>
   <property name="destroyable" value=""/>
   <property name="triggervalue" value="2"/>
  </properties>
 </tile>
 <tile id="3">
  <properties>
   <property name="destroyable" value=""/>
   <property name="triggervalue" value="3"/>
  </properties>
 </tile>
 <tile id="17">
  <properties>
   <property name="instancetype" value="once"/>
   <property name="triggervalue" value="1"/>
   <property name="worldvolume" value=""/>
  </properties>
 </tile>
 <tile id="32">
  <properties>
   <property name="levelcomplete" value=""/>
  </properties>
 </tile>
 <tile id="49">
  <properties>
   <property name="delay" value="0"/>
   <property name="door" value="closed"/>
   <property name="instancetype" value="once"/>
   <property name="triggervalue" value="1"/>
  </properties>
 </tile>
 <tile id="50">
  <properties>
   <property name="delay" value="0"/>
   <property name="door" value="closed"/>
   <property name="instancetype" value="once"/>
   <property name="triggervalue" value="2"/>
  </properties>
 </tile>
 <tile id="64">
  <properties>
   <property name="ladder" value=""/>
  </properties>
 </tile>
 <tile id="65">
  <properties>
   <property name="count" value="2"/>
   <property name="delay" value="0"/>
   <property name="enemyspawn" value="footsoldier"/>
   <property name="triggervalue" value="1"/>
  </properties>
 </tile>
 <tile id="80">
  <properties>
   <property name="ladder" value=""/>
   <property name="platform" value=""/>
  </properties>
 </tile>
 <tile id="96">
  <properties>
   <property name="platform" value=""/>
  </properties>
 </tile>
 <tile id="97">
  <properties>
   <property name="mover" value="backward"/>
   <property name="movex" value="0"/>
   <property name="movey" value="8"/>
   <property name="speed" value="2"/>
   <property name="triggervalue" value="0"/>
  </properties>
 </tile>
 <tile id="98">
  <properties>
   <property name="mover" value="backward"/>
   <property name="movex" value="0"/>
   <property name="movey" value="10"/>
   <property name="speed" value="2"/>
   <property name="triggervalue" value="0"/>
  </properties>
 </tile>
 <tile id="112">
  <properties>
   <property name="solid" value=""/>
  </properties>
 </tile>
 <tile id="113">
  <properties>
   <property name="mover" value="backward"/>
   <property name="movex" value="8"/>
   <property name="movey" value="0"/>
   <property name="speed" value="2"/>
   <property name="triggervalue" value="0"/>
  </properties>
 </tile>
 <tile id="114">
  <properties>
   <property name="mover" value=""/>
   <property name="movex" value="8"/>
   <property name="movey" value="0"/>
   <property name="speed" value="2"/>
   <property name="triggervalue" value="0"/>
  </properties>
 </tile>
 <tile id="115">
  <properties>
   <property name="mover" value=""/>
   <property name="movex" value="0"/>
   <property name="movey" value="36"/>
   <property name="speed" value="4"/>
   <property name="triggervalue" value="3"/>
  </properties>
 </tile>
 <tile id="128">
  <properties>
   <property name="conveyer" value="right"/>
  </properties>
 </tile>
 <tile id="129">
  <properties>
   <property name="flamerangle" value="0"/>
   <property name="flamerturret" value="left"/>
  </properties>
 </tile>
 <tile id="130">
  <properties>
   <property name="angle" value="90"/>
   <property name="turret" value="wallturret"/>
  </properties>
 </tile>
 <tile id="131">
  <properties>
   <property name="flamerturret" value="right"/>
  </properties>
 </tile>
 <tile id="132">
  <properties>
   <property name="angle" value="0"/>
   <property name="turret" value="wallturret"/>
  </properties>
 </tile>
 <tile id="144">
  <properties>
   <property name="conveyer" value="left"/>
  </properties>
 </tile>
 <tile id="160">
  <properties>
   <property name="death" value=""/>
  </properties>
 </tile>
 <tile id="161">
  <properties>
   <property name="powercable" value=""/>
   <property name="triggervalue" value="1"/>
  </properties>
 </tile>
 <tile id="162">
  <properties>
   <property name="powercable" value=""/>
   <property name="triggervalue" value="2"/>
  </properties>
 </tile>
 <tile id="177">
  <properties>
   <property name="crusher" value="hazard"/>
   <property name="delay" value="0"/>
   <property name="movex" value="0"/>
   <property name="movey" value="5"/>
   <property name="speed" value="0"/>
  </properties>
 </tile>
 <tile id="241">
  <properties>
   <property name="item" value="shotgun"/>
  </properties>
 </tile>
</tileset>
