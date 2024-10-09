package com.scrap.lib.spanishportal.parameters;

public enum Parameters {

    TIPO_OPERACION("Tipo de operación", "operation", "ComboBox", new String[]{"2", "1"}, new String[]{"Alquiler", "Venta"}),

    TIPO_INMUEBLE("Tipo de Inmueble", "typology", "ComboBox", new String[]{"10", "1", "7", "5", "6", "14", "4", "8", "12", "13"}, new String[]{"Obra nueva", "Viviendas", "Habitación", "Oficinas", "Locales o naves", "Traspasos", "Garajes", "Terrenos", "Trasteros", "Edificios"}),

    PRECIO_MIN("Precio Mínimo", "adfilter_pricemin", "TextField", new String[]{}, new String[]{}),
    PRECIO_MAX("Precio Máximo", "adfilter_pricemax", "TextField", new String[]{}, new String[]{}),

    AREA_MIN("Tamaño Mínimo", "adfilter_area", "TextField", new String[]{}, new String[]{}),
    AREA_MAX("Tamaño Máximo", "adfilter_areamax", "TextField", new String[]{}, new String[]{}),

    PISOS("Pisos", "adfilter_onlyflats", "CheckBox", new String[]{"1"}, new String[]{"Pisos"}),
    ATICOS("Áticos", "adfilter_penthouse", "CheckBox", new String[]{"1"}, new String[]{"Áticos"}),
    DUPLEX("Dúplex", "adfilter_duplex", "CheckBox", new String[]{"1"}, new String[]{"Dúplex"}),

    INDEPENDIENTES("Casas y chalets - Independientes", "adfilter_independent", "CheckBox", new String[]{"1"}, new String[]{"Independientes"}),
    PAREADOS("Casas y chalets - Pareados", "adfilter_semidetached", "CheckBox", new String[]{"1"}, new String[]{"Pareados"}),
    ADOSADOS("Casas y chalets - Adosados", "adfilter_terraced", "CheckBox", new String[]{"1"}, new String[]{"Adosados"}),
    RUSTICAS("Casas y chalets - Casas rústicas", "adfilter_countryhouses", "CheckBox", new String[]{"1"}, new String[]{"Casas rústicas"}),
    APARTAMENTOS("Otros - Apartamentos", "adfilter_apartment", "CheckBox", new String[]{"1"}, new String[]{"Apartamentos"}),
    VILLAS("Otros - Villas", "adfilter_villalabel", "CheckBox", new String[]{"1"}, new String[]{"Villas"}),
    BUNGALOWS("Otros - Bungalows", "adfilter_bungalow", "CheckBox", new String[]{"1"}, new String[]{"Bungalows"}),
    LOFTS("Otros - Lofts", "adfilter_loft", "CheckBox", new String[]{"1"}, new String[]{"Lofts"}),

    EQUIPAMIENTO("Equipamiento", "adfilter_amenity", "ComboBox", new String[]{"default", "2", "3"}, new String[]{"Indiferente", "Sólo cocina equipada", "Amueblado"}),

    HABITACIONES_0("0 habitaciones (estudios)", "adfilter_rooms_0", "CheckBox", new String[]{"1"}, new String[]{"0 Habitaciones (estudios)"}),
    HABITACIONES_1("1 habitación", "adfilter_rooms_1", "CheckBox", new String[]{"2"}, new String[]{"1 habitación"}),
    HABITACIONES_2("2 habitaciones", "adfilter_rooms_2", "CheckBox", new String[]{"2"}, new String[]{"2 habitaciones"}),
    HABITACIONES_3("3 habitaciones", "adfilter_rooms_3", "CheckBox", new String[]{"3"}, new String[]{"3 habitaciones"}),
    HABITACIONES_4("4 habitaciones o más", "adfilter_rooms_4_more", "CheckBox", new String[]{"4"}, new String[]{"4 habitaciones o más"}),


    BANYOS_1("1 baño", "adfilter_baths_1", "CheckBox", new String[]{"1"}, new String[]{"1 baño"}),
    BANYOS_2("2 baños", "adfilter_baths_2", "CheckBox", new String[]{"2"}, new String[]{"2 baños"}),
    BANYOS_3("3 baños o más", "adfilter_baths_3", "CheckBox", new String[]{"3"}, new String[]{"3 baños o más"}),


    OBRA_NUEVA("Obra nueva", "adfilter_newconstruction", "CheckBox", new String[]{"1"}, new String[]{"Obra nueva"}),
    BUEN_ESTADO("Buen estado", "adfilter_goodcondition", "CheckBox", new String[]{"3"}, new String[]{"Buen estado"}),
    A_REFORMAR("A reformar", "adfilter_toberestored", "CheckBox", new String[]{"2"}, new String[]{"A reformar"}),


    PLANTA("Admite mascotas", "adfilter_housingpetsallowed", "CheckBox", new String[]{"1"}, new String[]{"Admite mascotas"}),
    AIRE_ACONDICIONADO("Aire acondicionado", "adfilter_hasairconditioning", "CheckBox", new String[]{"1"}, new String[]{"Aire acondicionado"}),
    ARMARIOS_EMPOTRADOS("Armarios empotrados", "adfilter_wardrobes", "CheckBox", new String[]{"1"}, new String[]{"Armarios empotrados"}),
    ASCENSOR("Ascensor", "adfilter_lift", "CheckBox", new String[]{"1"}, new String[]{"Ascensor"}),
    BALCON("Balcón", "adfilter_balcony", "CheckBox", new String[]{"1"}, new String[]{"Balcón"}),
    TERRAZA("Terraza", "adfilter_hasterrace", "CheckBox", new String[]{"1"}, new String[]{"Terraza"}),
    EXTERIOR("Exterior", "adfilter_flatlocation", "CheckBox", new String[]{"1"}, new String[]{"Exterior"}),
    GARAJE("Garaje", "adfilter_parkingspace", "CheckBox", new String[]{"1"}, new String[]{"Garaje"}),
    JARDIN("Jardín", "adfilter_garden", "CheckBox", new String[]{"1"}, new String[]{"Jardín"}),
    PISCINA("Piscina", "adfilter_swimmingpool", "CheckBox", new String[]{"1"}, new String[]{"Piscina"}),
    TRASTERO("Trastero", "adfilter_boxroom", "CheckBox", new String[]{"1"}, new String[]{"Trastero"}),
    VIVIENDA_ACCESIBLE("Vivienda accesible", "adfilter_accessibleHousing", "CheckBox", new String[]{"1"}, new String[]{"Vivienda accesible"}),
    VISTAS_MAR("Vistas al mar", "adfilter_seaviews", "CheckBox", new String[]{"1"}, new String[]{"Vistas al mar"}),
    CHALET_LUJO("Chalet de lujo", "adfilter_luxury", "CheckBox", new String[]{"1"}, new String[]{"Chalet de lujo"}),

    TIPO_ANUNCIO("Tipo de Anuncio", "adfilter_agencyisabank", "CheckBox", new String[]{"1"}, new String[]{"De bancos"});

    private final String displayName;
    private final String key;
    private final String controlType;
    private final String[] values;
    private final String[] options;

    Parameters(String displayName, String key, String controlType, String[] values, String[] options) {
        this.displayName = displayName;
        this.key = key;
        this.controlType = controlType;
        this.values = values;
        this.options = options;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getKey() {
        return key;
    }

    public String getControlType() {
        return controlType;
    }

    public String[] getValues() {
        return values;
    }

    public String[] getOptions() {
        return options;
    }
}