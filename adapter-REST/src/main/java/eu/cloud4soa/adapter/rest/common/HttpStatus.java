/*
 *  Copyright 2013 Cloud4SOA, www.cloud4soa.eu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package eu.cloud4soa.adapter.rest.common;

/**
 * Hypertext Transfer Protocol (HTTP) Status Code Registry
 * 
 * @see <a
 *      href="http://www.iana.org/assignments/http-status-codes/http-status-codes.xml">
 *      http://www.iana.org/assignments/http-status-codes/http-status-codes.xml</a>
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public enum HttpStatus {

	Unknown(0),
	Unassigned_1(1), Unassigned_2(2), Unassigned_3(3), Unassigned_4(4), Unassigned_5(5), Unassigned_6(6), Unassigned_7(7), Unassigned_8(8), Unassigned_9(9), Unassigned_10(
			10), Unassigned_11(11), Unassigned_12(12), Unassigned_13(13), Unassigned_14(14), Unassigned_15(15), Unassigned_16(16), Unassigned_17(17), Unassigned_18(
			18), Unassigned_19(19), Unassigned_20(20), Unassigned_21(21), Unassigned_22(22), Unassigned_23(23), Unassigned_24(24), Unassigned_25(25), Unassigned_26(
			26), Unassigned_27(27), Unassigned_28(28), Unassigned_29(29), Unassigned_30(30), Unassigned_31(31), Unassigned_32(32), Unassigned_33(33), Unassigned_34(
			34), Unassigned_35(35), Unassigned_36(36), Unassigned_37(37), Unassigned_38(38), Unassigned_39(39), Unassigned_40(40), Unassigned_41(41), Unassigned_42(
			42), Unassigned_43(43), Unassigned_44(44), Unassigned_45(45), Unassigned_46(46), Unassigned_47(47), Unassigned_48(48), Unassigned_49(49), Unassigned_50(
			50), Unassigned_51(51), Unassigned_52(52), Unassigned_53(53), Unassigned_54(54), Unassigned_55(55), Unassigned_56(56), Unassigned_57(57), Unassigned_58(
			58), Unassigned_59(59), Unassigned_60(60), Unassigned_61(61), Unassigned_62(62), Unassigned_63(63), Unassigned_64(64), Unassigned_65(65), Unassigned_66(
			66), Unassigned_67(67), Unassigned_68(68), Unassigned_69(69), Unassigned_70(70), Unassigned_71(71), Unassigned_72(72), Unassigned_73(73), Unassigned_74(
			74), Unassigned_75(75), Unassigned_76(76), Unassigned_77(77), Unassigned_78(78), Unassigned_79(79), Unassigned_80(80), Unassigned_81(81), Unassigned_82(
			82), Unassigned_83(83), Unassigned_84(84), Unassigned_85(85), Unassigned_86(86), Unassigned_87(87), Unassigned_88(88), Unassigned_89(89), Unassigned_90(
			90), Unassigned_91(91), Unassigned_92(92), Unassigned_93(93), Unassigned_94(94), Unassigned_95(95), Unassigned_96(96), Unassigned_97(97), Unassigned_98(
			98), Unassigned_99(99), Continue(100), Switching_Protocols(101), Processing(102), Unassigned_103(103), Unassigned_104(104), Unassigned_105(105), Unassigned_106(
			106), Unassigned_107(107), Unassigned_108(108), Unassigned_109(109), Unassigned_110(110), Unassigned_111(111), Unassigned_112(112), Unassigned_113(
			113), Unassigned_114(114), Unassigned_115(115), Unassigned_116(116), Unassigned_117(117), Unassigned_118(118), Unassigned_119(119), Unassigned_120(
			120), Unassigned_121(121), Unassigned_122(122), Unassigned_123(123), Unassigned_124(124), Unassigned_125(125), Unassigned_126(126), Unassigned_127(
			127), Unassigned_128(128), Unassigned_129(129), Unassigned_130(130), Unassigned_131(131), Unassigned_132(132), Unassigned_133(133), Unassigned_134(
			134), Unassigned_135(135), Unassigned_136(136), Unassigned_137(137), Unassigned_138(138), Unassigned_139(139), Unassigned_140(140), Unassigned_141(
			141), Unassigned_142(142), Unassigned_143(143), Unassigned_144(144), Unassigned_145(145), Unassigned_146(146), Unassigned_147(147), Unassigned_148(
			148), Unassigned_149(149), Unassigned_150(150), Unassigned_151(151), Unassigned_152(152), Unassigned_153(153), Unassigned_154(154), Unassigned_155(
			155), Unassigned_156(156), Unassigned_157(157), Unassigned_158(158), Unassigned_159(159), Unassigned_160(160), Unassigned_161(161), Unassigned_162(
			162), Unassigned_163(163), Unassigned_164(164), Unassigned_165(165), Unassigned_166(166), Unassigned_167(167), Unassigned_168(168), Unassigned_169(
			169), Unassigned_170(170), Unassigned_171(171), Unassigned_172(172), Unassigned_173(173), Unassigned_174(174), Unassigned_175(175), Unassigned_176(
			176), Unassigned_177(177), Unassigned_178(178), Unassigned_179(179), Unassigned_180(180), Unassigned_181(181), Unassigned_182(182), Unassigned_183(
			183), Unassigned_184(184), Unassigned_185(185), Unassigned_186(186), Unassigned_187(187), Unassigned_188(188), Unassigned_189(189), Unassigned_190(
			190), Unassigned_191(191), Unassigned_192(192), Unassigned_193(193), Unassigned_194(194), Unassigned_195(195), Unassigned_196(196), Unassigned_197(
			197), Unassigned_198(198), Unassigned_199(199), OK(200), Created(201), Accepted(202), Non_Authoritative_Information(203), No_Content(204), Reset_Content(
			205), Partial_Content(206), Multi_Status(207), Already_Reported(208), Unassigned_209(209), Unassigned_210(210), Unassigned_211(211), Unassigned_212(
			212), Unassigned_213(213), Unassigned_214(214), Unassigned_215(215), Unassigned_216(216), Unassigned_217(217), Unassigned_218(218), Unassigned_219(
			219), Unassigned_220(220), Unassigned_221(221), Unassigned_222(222), Unassigned_223(223), Unassigned_224(224), Unassigned_225(225), IM_Used(226), Unassigned_227(
			227), Unassigned_228(228), Unassigned_229(229), Unassigned_230(230), Unassigned_231(231), Unassigned_232(232), Unassigned_233(233), Unassigned_234(
			234), Unassigned_235(235), Unassigned_236(236), Unassigned_237(237), Unassigned_238(238), Unassigned_239(239), Unassigned_240(240), Unassigned_241(
			241), Unassigned_242(242), Unassigned_243(243), Unassigned_244(244), Unassigned_245(245), Unassigned_246(246), Unassigned_247(247), Unassigned_248(
			248), Unassigned_249(249), Unassigned_250(250), Unassigned_251(251), Unassigned_252(252), Unassigned_253(253), Unassigned_254(254), Unassigned_255(
			255), Unassigned_256(256), Unassigned_257(257), Unassigned_258(258), Unassigned_259(259), Unassigned_260(260), Unassigned_261(261), Unassigned_262(
			262), Unassigned_263(263), Unassigned_264(264), Unassigned_265(265), Unassigned_266(266), Unassigned_267(267), Unassigned_268(268), Unassigned_269(
			269), Unassigned_270(270), Unassigned_271(271), Unassigned_272(272), Unassigned_273(273), Unassigned_274(274), Unassigned_275(275), Unassigned_276(
			276), Unassigned_277(277), Unassigned_278(278), Unassigned_279(279), Unassigned_280(280), Unassigned_281(281), Unassigned_282(282), Unassigned_283(
			283), Unassigned_284(284), Unassigned_285(285), Unassigned_286(286), Unassigned_287(287), Unassigned_288(288), Unassigned_289(289), Unassigned_290(
			290), Unassigned_291(291), Unassigned_292(292), Unassigned_293(293), Unassigned_294(294), Unassigned_295(295), Unassigned_296(296), Unassigned_297(
			297), Unassigned_298(298), Unassigned_299(299), Multiple_Choices(300), Moved_Permanently(301), Found(302), See_Other(303), Not_Modified(304), Use_Proxy(
			305), Reserved(306), Temporary_Redirect(307), Unassigned_308(308), Unassigned_309(309), Unassigned_310(310), Unassigned_311(311), Unassigned_312(
			312), Unassigned_313(313), Unassigned_314(314), Unassigned_315(315), Unassigned_316(316), Unassigned_317(317), Unassigned_318(318), Unassigned_319(
			319), Unassigned_320(320), Unassigned_321(321), Unassigned_322(322), Unassigned_323(323), Unassigned_324(324), Unassigned_325(325), Unassigned_326(
			326), Unassigned_327(327), Unassigned_328(328), Unassigned_329(329), Unassigned_330(330), Unassigned_331(331), Unassigned_332(332), Unassigned_333(
			333), Unassigned_334(334), Unassigned_335(335), Unassigned_336(336), Unassigned_337(337), Unassigned_338(338), Unassigned_339(339), Unassigned_340(
			340), Unassigned_341(341), Unassigned_342(342), Unassigned_343(343), Unassigned_344(344), Unassigned_345(345), Unassigned_346(346), Unassigned_347(
			347), Unassigned_348(348), Unassigned_349(349), Unassigned_350(350), Unassigned_351(351), Unassigned_352(352), Unassigned_353(353), Unassigned_354(
			354), Unassigned_355(355), Unassigned_356(356), Unassigned_357(357), Unassigned_358(358), Unassigned_359(359), Unassigned_360(360), Unassigned_361(
			361), Unassigned_362(362), Unassigned_363(363), Unassigned_364(364), Unassigned_365(365), Unassigned_366(366), Unassigned_367(367), Unassigned_368(
			368), Unassigned_369(369), Unassigned_370(370), Unassigned_371(371), Unassigned_372(372), Unassigned_373(373), Unassigned_374(374), Unassigned_375(
			375), Unassigned_376(376), Unassigned_377(377), Unassigned_378(378), Unassigned_379(379), Unassigned_380(380), Unassigned_381(381), Unassigned_382(
			382), Unassigned_383(383), Unassigned_384(384), Unassigned_385(385), Unassigned_386(386), Unassigned_387(387), Unassigned_388(388), Unassigned_389(
			389), Unassigned_390(390), Unassigned_391(391), Unassigned_392(392), Unassigned_393(393), Unassigned_394(394), Unassigned_395(395), Unassigned_396(
			396), Unassigned_397(397), Unassigned_398(398), Unassigned_399(399), Bad_Request(400), Unauthorized(401), Payment_Required(402), Forbidden(403), Not_Found(
			404), Method_Not_Allowed(405), Not_Acceptable(406), Proxy_Authentication_Required(407), Request_Timeout(408), Conflict(409), Gone(410), Length_Required(
			411), Precondition_Failed(412), Request_Entity_Too_Large(413), Request_URI_Too_Long(414), Unsupported_Media_Type(415), Requested_Range_Not_Satisfiable(
			416), Expectation_Failed(417), Unassigned_418(418), Unassigned_419(419), Unassigned_420(420), Unassigned_421(421), Unprocessable_Entity(422), Locked(
			423), Failed_Dependency(424), Reserved_for_WebDAV_advanced_collections_expired_proposal(425), Upgrade_Required(426), Unassigned_427(427), Precondition_Required(
			428), Too_Many_Requests(429), Unassigned_430(430), Request_Header_Fields_Too_Large(431), Unassigned_432(432), Unassigned_433(433), Unassigned_434(
			434), Unassigned_435(435), Unassigned_436(436), Unassigned_437(437), Unassigned_438(438), Unassigned_439(439), Unassigned_440(440), Unassigned_441(
			441), Unassigned_442(442), Unassigned_443(443), Unassigned_444(444), Unassigned_445(445), Unassigned_446(446), Unassigned_447(447), Unassigned_448(
			448), Unassigned_449(449), Unassigned_450(450), Unassigned_451(451), Unassigned_452(452), Unassigned_453(453), Unassigned_454(454), Unassigned_455(
			455), Unassigned_456(456), Unassigned_457(457), Unassigned_458(458), Unassigned_459(459), Unassigned_460(460), Unassigned_461(461), Unassigned_462(
			462), Unassigned_463(463), Unassigned_464(464), Unassigned_465(465), Unassigned_466(466), Unassigned_467(467), Unassigned_468(468), Unassigned_469(
			469), Unassigned_470(470), Unassigned_471(471), Unassigned_472(472), Unassigned_473(473), Unassigned_474(474), Unassigned_475(475), Unassigned_476(
			476), Unassigned_477(477), Unassigned_478(478), Unassigned_479(479), Unassigned_480(480), Unassigned_481(481), Unassigned_482(482), Unassigned_483(
			483), Unassigned_484(484), Unassigned_485(485), Unassigned_486(486), Unassigned_487(487), Unassigned_488(488), Unassigned_489(489), Unassigned_490(
			490), Unassigned_491(491), Unassigned_492(492), Unassigned_493(493), Unassigned_494(494), Unassigned_495(495), Unassigned_496(496), Unassigned_497(
			497), Unassigned_498(498), Unassigned_499(499), Internal_Server_Error(500), Not_Implemented(501), Bad_Gateway(502), Service_Unavailable(503), Gateway_Timeout(
			504), HTTP_Version_Not_Supported(505), Variant_Also_Negotiates(506), Insufficient_Storage(507), Loop_Detected(508), Unassigned_509(509), Not_Extended(
			510), Network_Authentication_Required(511), Unassigned_512(512), Unassigned_513(513), Unassigned_514(514), Unassigned_515(515), Unassigned_516(516), Unassigned_517(
			517), Unassigned_518(518), Unassigned_519(519), Unassigned_520(520), Unassigned_521(521), Unassigned_522(522), Unassigned_523(523), Unassigned_524(
			524), Unassigned_525(525), Unassigned_526(526), Unassigned_527(527), Unassigned_528(528), Unassigned_529(529), Unassigned_530(530), Unassigned_531(
			531), Unassigned_532(532), Unassigned_533(533), Unassigned_534(534), Unassigned_535(535), Unassigned_536(536), Unassigned_537(537), Unassigned_538(
			538), Unassigned_539(539), Unassigned_540(540), Unassigned_541(541), Unassigned_542(542), Unassigned_543(543), Unassigned_544(544), Unassigned_545(
			545), Unassigned_546(546), Unassigned_547(547), Unassigned_548(548), Unassigned_549(549), Unassigned_550(550), Unassigned_551(551), Unassigned_552(
			552), Unassigned_553(553), Unassigned_554(554), Unassigned_555(555), Unassigned_556(556), Unassigned_557(557), Unassigned_558(558), Unassigned_559(
			559), Unassigned_560(560), Unassigned_561(561), Unassigned_562(562), Unassigned_563(563), Unassigned_564(564), Unassigned_565(565), Unassigned_566(
			566), Unassigned_567(567), Unassigned_568(568), Unassigned_569(569), Unassigned_570(570), Unassigned_571(571), Unassigned_572(572), Unassigned_573(
			573), Unassigned_574(574), Unassigned_575(575), Unassigned_576(576), Unassigned_577(577), Unassigned_578(578), Unassigned_579(579), Unassigned_580(
			580), Unassigned_581(581), Unassigned_582(582), Unassigned_583(583), Unassigned_584(584), Unassigned_585(585), Unassigned_586(586), Unassigned_587(
			587), Unassigned_588(588), Unassigned_589(589), Unassigned_590(590), Unassigned_591(591), Unassigned_592(592), Unassigned_593(593), Unassigned_594(
			594), Unassigned_595(595), Unassigned_596(596), Unassigned_597(597), Unassigned_598(598), Unassigned_599(599);

	private int code;

	private HttpStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static HttpStatus getStatus(int code) {
		if (code < 0) {
			return Unknown;
		}else if(code >= 600){
			return Unassigned_599;
		}
		HttpStatus status = (code > HttpStatus.values().length - 1 ? Unassigned_599 : HttpStatus.values()[code]);
		return status;
	}

	@Override
	public String toString() {
		if (this.name().startsWith("Unassigned_")) {
			return this.name().replaceAll("_\\d+", "");
		}
		return this.name().replace("_", " ") + " (" + (this.equals(Unknown) ? "0" : (this.ordinal())) + ")";
	}
}
