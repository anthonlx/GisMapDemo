package com.kanq.gismapdemo;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

	private MapView mapView = null;
	private static final String path = "/sdcard/TPK";

//	final static String ARG_POSITION = "position";

	private GraphicsLayer firstGeomLayer = null;

//	private GraphicsLayer resultGeomLayer = null;

	private RadioGroup geomType = null;

	private GEOMETRY_TYPE firstGeoType = GEOMETRY_TYPE.point;

	private boolean isStartPointSet1 = false;

//	private int selectedGeomID = 0;
//
//	private double bufferDist = 3000;

	//空间参考
	private SpatialReference spatialRef;

	//几何操作工具类
	private GeometryEngine geometryEngine;

	Geometry firstGeometry = null;

	volatile int countTap = 0;

	private String temp;

//	int geomNumWorkon = -1;

	enum GEOMETRY_TYPE {
		point, polyline, polygon
	}

	boolean enableSketching = true;// 这是允许绘图么

	private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8;
	private ArcGISLocalTiledLayer loc1, loc2, loc3, loc4, loc5, loc6, loc7, loc8;
	private boolean b1,b2,b3,b4,b5,b6,b7,b8 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.mapView = (MapView) this.findViewById(R.id.map);// 设置UI和代码绑定
//		 String
//		 strMapUrl="http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer";
//		 ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer = new
//		 ArcGISTiledMapServiceLayer(strMapUrl);
//		 mapView.addLayer(arcGISTiledMapServiceLayer);

		mapView.setMapBackground(0xffffff, Color.TRANSPARENT, 0, 0);
		mapView.setEsriLogoVisible(false);
		ArcGISRuntime.setClientId("xQVPBjsik3SvzLVK");
//
		loc1 = new ArcGISLocalTiledLayer(path + "/tdytq.tpk");
		loc2 = new ArcGISLocalTiledLayer(path + "/xzqh.tpk");
		loc3 = new ArcGISLocalTiledLayer(path + "/2015_tdlyxz.tpk");
		loc4 = new ArcGISLocalTiledLayer(path + "/2016_jctb.tpk");
		loc5 = new ArcGISLocalTiledLayer(path + "/2016_ygyx.tpk");
		loc6 = new ArcGISLocalTiledLayer(path + "/gdba.tpk");
		loc7 = new ArcGISLocalTiledLayer(path + "/jsydgzq.tpk");
		loc8 = new ArcGISLocalTiledLayer(path + "/jsydsp.tpk");
//
		//设置gps坐标系
		spatialRef = SpatialReference.create(SpatialReference.WKID_WGS84);
		geometryEngine = new GeometryEngine();
		Log.e("是否是wgs84坐标",spatialRef.isWGS84()+"");
//
		temp = "[[[116.5555797296017, 39.29840218518259],[116.55560580887264, 39.297970850020849],[116.55564708429593, 39.297485428539008],[116.55567467501479, 39.297006316586138],[116.5557048991158, 39.29654205675713],[116.55573475694054, 39.2961301797642],[116.55574640749659, 39.296130488131449],[116.55626212025887, 39.296148275988297],[116.55658551466604, 39.29617635864222],[116.55661146821395, 39.2961786120132],[116.55706942426304, 39.29620672380181],[116.55762240004575, 39.296235193960807],[116.5580058503503, 39.29623928759038],[116.55857592644499, 39.29624672409204],[116.55939573990254, 39.29626900943023],[116.559269079109, 39.29648834621977],[116.5591641300464, 39.2967263343877],[116.55914416112222, 39.296771615610449],[116.55906116704408, 39.29714082583442],[116.5590562378497, 39.29724968571573],[116.55906631202, 39.29730709905086],[116.55910953455012, 39.29755342503837],[116.55910417080315, 39.29773156871156],[116.55907253407504, 39.29800353490665],[116.55905928694333, 39.29811741993093],[116.5590630157596, 39.29819991778885],[116.55906464084952, 39.298202904436788],[116.55903764850254, 39.29825486131134],[116.55899808097296, 39.298277177284337],[116.5589165485606, 39.298285037086909],[116.55758695874174, 39.298338191677817],\n" +
				"[116.5575426119642, 39.29833974173509],[116.5568626668607, 39.2983635046099],[116.55634465137526, 39.29838763117256],[116.5555797296017, 39.29840218518259]],[[116.55614987580323, 39.29694509380354],[116.55656425478623, 39.29693247106647],[116.55657330404651, 39.29646384290145],[116.55615298452918, 39.29645277291817],[116.55614987580323, 39.29694509380354]]]";
//		//先加载绘图图层，后面添加图层都以addLayer(layer,mapview.getLayers().length-1)加载，以确保绘图图层永远在最上层
		initDraw();
		initButton();

	}

//	private void init() {
//		Envelope env = new Envelope(12957628.58241, 48642.124, 12957628.58241, 48642.124);
//		mapView.setExtent(env);
//		mapView.setScale(295828763);
//		mapView.setResolution(9783);
//		mapView.setMapBackground(0xffffff, Color.TRANSPARENT, 0, 0);
//		mapView.setAllowRotationByPinch(true);
//		mapView.setRotationAngle(15.0);
//	}

	private void initDraw() {
		//要确保绘图图层永远在最上层，不然会导致选择的图层遮盖绘图图层
		firstGeomLayer = new GraphicsLayer();// 难道必须有绘图图层才能在上面绘图
		mapView.addLayer(firstGeomLayer);


		/**
		 * Single tap listener for MapView ***************
		 */
		mapView.setOnSingleTapListener(new OnSingleTapListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSingleTap(float x, float y) {
				// Check if sketching is enabled
				if (enableSketching) {
					try {
						singleTapAct(x, y);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} // 监听地图上的点击事件
			}
		});

	}

	void singleTapAct(float x, float y) throws Exception {
		countTap++;
		Point point = mapView.toMapPoint(x, y);// 获取这个坐标对应的Point
		Log.d("sigle tap on screen:", "[" + x + "," + y + "]");
		Log.d("sigle tap on map:", "[" + point.getX() + "," + point.getY() + "]");

		if (firstGeometry == null) {
			if (firstGeoType == GEOMETRY_TYPE.point) {// 如果是point，那么firstGeometry就等于point
				firstGeometry = point;

			} else if (firstGeoType == GEOMETRY_TYPE.polygon) {// 如果是polygon，
				firstGeometry = new Polygon();
				((MultiPath) firstGeometry).startPath(point);// 以当前这个point为开始路径么
				isStartPointSet1 = true;
				Log.e("geometry step " + countTap,
						GeometryEngine.geometryToJson(mapView.getSpatialReference(), firstGeometry));

			} else if (firstGeoType == GEOMETRY_TYPE.polyline) {
				isStartPointSet1 = true;
				firstGeometry = new Polyline();// 将firstGeometry初始化为Polyline
				((MultiPath) firstGeometry).startPath(point);
			}

		}

		if (firstGeoType == null)
			return;
		int color1 = Color.BLUE;
		drawGeomOnGraphicLyr(firstGeometry, firstGeomLayer, point, firstGeoType, color1, isStartPointSet1);
		Log.d("geometry step " + countTap, GeometryEngine.geometryToJson(mapView.getSpatialReference(), firstGeometry));// 每一次的点击在这里都会体现

	}

	/**
	 * 通过firstGeoType来设置绘图的类型
	 */
	void initButton() {// 设置按钮的类型

		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.e("geometry",geometryEngine.geometryToJson(spatialRef,firstGeometry).toString()
				);
			}
		});

		findViewById(R.id.loc).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				Location location = mapView.getLocationDisplayManager().getLocation();
//				Toast.makeText(MainActivity.this,location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_LONG).show();
				try {
					JSONArray arr = new JSONArray(temp);
					Log.e("arr.length",arr.length()+"");
					List<Polygon> polygonList = new ArrayList();
					for(int i=0;i<arr.length();i++){
						String s = arr.get(i).toString();
						s = s.substring(1,s.length()-2);
						String[] array = s.split("],");
						Polygon polygon = new Polygon();
						for(int j=0;j<array.length;j++){
							String ss = array[j];
							ss = ss.substring(1);
							String[] array2 = ss.split(",");
							if(j==0){
								polygon.startPath(new Point(Double.parseDouble(array2[0]),Double.parseDouble(array2[1])));
							}else{
								polygon.lineTo(new Point(Double.parseDouble(array2[0]),Double.parseDouble(array2[1])));
							}
						}
						polygonList.add(polygon);
						GeometryUtil.addPolygon(polygon,firstGeomLayer);
					}
					mapView.setExtent(polygonList.get(0));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		geomType = (RadioGroup) findViewById(R.id.geometrytype);

		geomType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// Set the geometry type to draw on the map

				switch (checkedId) {
					case R.id.point:
						ResetLayer();
						firstGeoType = GEOMETRY_TYPE.point;
						break;
					case R.id.line:
						ResetLayer();
						firstGeoType = GEOMETRY_TYPE.polyline;
						break;
					case R.id.polygon:
						ResetLayer();
						firstGeoType = GEOMETRY_TYPE.polygon;
						break;
					case R.id.notdo:
						ResetLayer();
						break;
				}

			}
		});

		cb1 = (CheckBox) findViewById(R.id.no1);
		cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					mapView.addLayer(loc1,mapView.getLayers().length-1);
				}else{
					mapView.removeLayer(loc1);
				}
				Log.e("loc1",""+mapView.getLayers().length);
			}
		});
		cb2 = (CheckBox) findViewById(R.id.no2);
		cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					mapView.addLayer(loc2,mapView.getLayers().length-1);
				}else{
					mapView.removeLayer(loc2);
				}
				Log.e("loc2",""+mapView.getLayers().length);
			}
		});
		cb3 = (CheckBox) findViewById(R.id.no3);
		cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					mapView.addLayer(loc3,mapView.getLayers().length-1);
				}else{
					mapView.removeLayer(loc3);
				}
				Log.e("loc3",""+mapView.getLayers().length);
			}
		});
		cb4 = (CheckBox) findViewById(R.id.no4);
		cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					mapView.addLayer(loc4,mapView.getLayers().length-1);
				}else{
					mapView.removeLayer(loc4);
				}
				Log.e("loc4",""+mapView.getLayers().length);
			}
		});
		cb5 = (CheckBox) findViewById(R.id.no5);
		cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					mapView.addLayer(loc5,mapView.getLayers().length-1);
				}else{
					mapView.removeLayer(loc5);
				}
			}
		});
		cb6 = (CheckBox) findViewById(R.id.no6);
		cb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					mapView.addLayer(loc6,mapView.getLayers().length-1);
				}else{
					mapView.removeLayer(loc6);
				}
			}
		});
		cb7 = (CheckBox) findViewById(R.id.no7);
		cb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					mapView.addLayer(loc7,mapView.getLayers().length-1);
				}else{
					mapView.removeLayer(loc7);
				}
			}
		});
		cb8 = (CheckBox) findViewById(R.id.no8);
		cb8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					mapView.addLayer(loc8,mapView.getLayers().length-1);
				}else{
					mapView.removeLayer(loc8);
				}
			}
		});

		cb1.setChecked(true);

	}

	/**
	 * 绘图的关键方法 Geometry代表point、
	 */
	void drawGeomOnGraphicLyr(Geometry geometryToDraw, GraphicsLayer glayer, Point point, GEOMETRY_TYPE geoTypeToDraw,
							  int color, boolean startPointSet) {

		if (geoTypeToDraw == GEOMETRY_TYPE.point) {
			geometryToDraw = point;

		} else {

			if (startPointSet) {

				if (geoTypeToDraw == GEOMETRY_TYPE.polygon) {
					((Polygon) geometryToDraw).lineTo(point);// Adds a Line
					// Segment to
					// the given end
					// point.
				} else if (geoTypeToDraw == GEOMETRY_TYPE.polyline) {
					((Polyline) geometryToDraw).lineTo(point);
				}

			}
		}

		Geometry[] geoms = new Geometry[1];
		geoms[0] = geometryToDraw;

		try {
			glayer.removeAll();// 为什么这里会有移除的做法呢
			GeometryUtil.highlightGeometriesWithColor(geoms, glayer, color);

			GeometryUtil.addPointToGraphicsLayer(point, glayer, color, 10, STYLE.CIRCLE);// 增加一个点，不然画线和画面的时候，看不到第一个起点
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void ResetLayer() {
		firstGeometry = null;

		firstGeomLayer.removeAll();

//		resultGeomLayer.removeAll();

		enableSketching = true;

	}

	//这样设置，图层在一直叠加，并没有被清理掉
//	@Override
//	public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
//		// TODO Auto-generated method stub
//		Log.e("",paramCompoundButton+"");
//		if (cb1.isChecked()) {
//			b1 = true;
//			mapView.addLayer(loc1);
//		} else {
//			if(b1){
//				b1 = false;
//				mapView.removeLayer(loc1);
//			}
//
//		}
//
//		if (cb2.isChecked()) {
//			b2 = true;
//			mapView.addLayer(loc2);
//		} else {
//			if(b2){
//				b2 = false;
//				mapView.removeLayer(loc2);
//			}
//
//		}
//		if (cb3.isChecked()) {
//			b3 = true;
//			mapView.addLayer(loc3);
//		} else {
//			if(b3){
//				b3 = false;
//				mapView.removeLayer(loc3);
//			}
//
//		}
//		if (cb4.isChecked()) {
//			b4 = true;
//			mapView.addLayer(loc4);
//		} else {
//			if(b4){
//				b4 = false;
//				mapView.removeLayer(loc4);
//			}
//
//		}
//		if (cb5.isChecked()) {
//			b5 = true;
//			mapView.addLayer(loc5);
//		} else {
//			if(b5){
//				b5 = false;
//				mapView.removeLayer(loc5);
//			}
//
//		}
//		if (cb6.isChecked()) {
//			b6 = true;
//			mapView.addLayer(loc6);
//		} else {
//			if(b6){
//				b6 = false;
//				mapView.removeLayer(loc6);
//			}
//
//		}
//	}
}
