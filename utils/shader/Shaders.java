package ru.luminar.utils.shader;

import ru.luminar.utils.shader.shaders.AlphaGlsl;
import ru.luminar.utils.shader.shaders.ContrastGlsl;
import ru.luminar.utils.shader.shaders.CornerRoundShaderGlsl;
import ru.luminar.utils.shader.shaders.FontGlsl;
import ru.luminar.utils.shader.shaders.GaussianBloomGlsl;
import ru.luminar.utils.shader.shaders.GradientRoundGlsl;
import ru.luminar.utils.shader.shaders.GradientRoundShaderGlsl;
import ru.luminar.utils.shader.shaders.KawaseDownGlsl;
import ru.luminar.utils.shader.shaders.KawaseUpGlsl;
import ru.luminar.utils.shader.shaders.MaskGlsl;
import ru.luminar.utils.shader.shaders.OutlineGlsl;
import ru.luminar.utils.shader.shaders.RoundedGlsl;
import ru.luminar.utils.shader.shaders.RoundedOutGlsl;
import ru.luminar.utils.shader.shaders.SmoothGlsl;
import ru.luminar.utils.shader.shaders.VertexGlsl;
import ru.luminar.utils.shader.shaders.WhiteGlsl;

public class Shaders {
   private IShader gradientRound = new GradientRoundGlsl();
   private static Shaders Instance = new Shaders();
   private IShader font = new FontGlsl();
   private IShader vertex = new VertexGlsl();
   private IShader rounded = new RoundedGlsl();
   private IShader roundedout = new RoundedOutGlsl();
   private IShader smooth = new SmoothGlsl();
   private IShader white = new WhiteGlsl();
   private IShader alpha = new AlphaGlsl();
   private IShader gaussianbloom = new GaussianBloomGlsl();
   private IShader kawaseUp = new KawaseUpGlsl();
   private IShader kawaseDown = new KawaseDownGlsl();
   private IShader outline = new OutlineGlsl();
   private IShader contrast = new ContrastGlsl();
   private IShader mask = new MaskGlsl();
   private IShader gradientRoundShader = new GradientRoundShaderGlsl();
   private IShader cornerRoundShader = new CornerRoundShaderGlsl();

   public static Shaders getInstance() {
      return Instance;
   }

   public IShader getGradientRound() {
      return this.gradientRound;
   }

   public IShader getFont() {
      return this.font;
   }

   public IShader getVertex() {
      return this.vertex;
   }

   public IShader getRounded() {
      return this.rounded;
   }

   public IShader getRoundedout() {
      return this.roundedout;
   }

   public IShader getSmooth() {
      return this.smooth;
   }

   public IShader getWhite() {
      return this.white;
   }

   public IShader getAlpha() {
      return this.alpha;
   }

   public IShader getGaussianbloom() {
      return this.gaussianbloom;
   }

   public IShader getKawaseUp() {
      return this.kawaseUp;
   }

   public IShader getKawaseDown() {
      return this.kawaseDown;
   }

   public IShader getOutline() {
      return this.outline;
   }

   public IShader getContrast() {
      return this.contrast;
   }

   public IShader getMask() {
      return this.mask;
   }

   public IShader getGradientRoundShader() {
      return this.gradientRoundShader;
   }

   public IShader getCornerRoundShader() {
      return this.cornerRoundShader;
   }
}
