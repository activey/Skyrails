// based on "Hue Rotation while preserving luminance"
// http://www.graficaobscura.com/matrix/index.html
// Paul Haeberli
//
// initial port to glsl by vade
// matrix math fixes, and made functional by PKM, see:
// http://www.cycling74.com/forums/index.php?t=post&reply_to=149584&rid=2009&S=9bea55000f1cb6eaa4d776366d837af3
// for more info.

uniform float hue;

uniform sampler2D therender;

// no need to compute these...
const float sqrt2 = 1.414213562373095;
const float sqrt3 = 1.732050807568877;
const float oneoversqrt2 = 0.707106781186548;
const float oneoversqrt3 = 0.577350269189626;

const vec4 lumcoeff = vec4(0.3086,0.6094,0.0820, 1.0);

const float zsx = lumcoeff.x/lumcoeff.z;
const float zsy = lumcoeff.y/lumcoeff.z;

// matrix functions.

mat4 ident = mat4(  1.0, 0.0, 0.0, 0.0,
					0.0, 1.0, 0.0, 0.0,
					0.0, 0.0, 1.0, 0.0,
					0.0, 0.0, 0.0, 1.0);


// red
mat4 rotateXMat(float rs, float rc)
{
return mat4(1.0, 0.0, 0.0, 0.0,
			0.0, rc, rs, 0.0,
			0.0, -rs, rc, 0.0,
			0.0, 0.0, 0.0, 1.0);
}

// green
mat4 rotateYMat(float rs, float rc)
{
return mat4(rc, 0.0, -rs, 0.0,
			0.0, 1.0, 0.0, 0.0,
			rs, 0.0, rc, 0.0,
			0.0, 0.0, 0.0, 1.0);
}	

// blue	
mat4 rotateZMat( float rs, float rc)
{
return mat4(rc, rs, 0.0, 0.0,
			-rs, rc, 0.0, 0.0,
			0.0, 0.0, 1.0, 0.0,
			0.0, 0.0, 0.0, 1.0);	
}

mat4 shearZMatrix(float dx, float dy)
{
return mat4(1.0, 0.0, dx, 0.0,
			0.0, 1.0, dy, 0.0,
			0.0, 0.0, 1.0, 0.0,
			0.0, 0.0, 0.0, 1.0);
}

vec4 xformpnt(mat4 matrix, vec4 inputVec)
{

return vec4(inputVec.x*matrix[0][0] + inputVec.y*matrix[1][0] +
			inputVec.z*matrix[2][0] + matrix[3][0],
			inputVec.x*matrix[0][1] + inputVec.y*matrix[1][1] +
			inputVec.z*matrix[2][1] + matrix[3][1],
			inputVec.x*matrix[0][2] + inputVec.y*matrix[1][2] +
			inputVec.z*matrix[2][2] + matrix[3][2], 1.0);
}


void main (void)
{

   vec4 inputVec = texture2D(therender,gl_TexCoord[0].st);

//	vec4 inputVec = texture2DRect(tex0, texcoord0);

// setup our transform mat - cant convert a const
mat4 transformMat = ident;

// rotate grey vector into positive Z
transformMat = rotateXMat(oneoversqrt2, oneoversqrt2) * transformMat;
transformMat = rotateYMat(-oneoversqrt3, sqrt2/sqrt3) * transformMat;

// shear to preserve lumanince
vec4 something = xformpnt(transformMat, lumcoeff);
float zsx = something.x/something.z;
float zsy = something.y/something.z;
transformMat = shearZMatrix(zsx,zsy) * transformMat;

// rotate hue, make angle input in degrees

float hue_rs = sin(radians(hue));
float hue_rc = cos(radians(hue));
transformMat = rotateZMat(hue_rs, hue_rc) * transformMat;

// unshear to preserve luminance
transformMat = shearZMatrix(-zsx,-zsy) * transformMat;

// unrotate
transformMat = rotateYMat(oneoversqrt3, sqrt2/sqrt3 ) * transformMat;
transformMat = rotateXMat(-oneoversqrt2, oneoversqrt2) * transformMat;

// do the hue transformation on our pixels
inputVec = transformMat * inputVec;

// convert to a vec with proper rgb values from our mat4.

//	gl_FragColor = inputVec;
   gl_FragColor= vec4( 1.0 - inputVec.r, 1.0 - inputVec.g, 1.0 - inputVec.b, 1.0 ); 
}
