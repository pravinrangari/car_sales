# Importing required libraries
import streamlit as st           # Streamlit is used to create web apps easily
import requests                 # Used to call REST APIs (Spring Boot backend)
import pandas as pd             # Used for data manipulation and DataFrame handling
import plotly.express as px     # Used for creating interactive charts

# ---------------------------
# Page Configuration
# ---------------------------
# This sets the basic configuration of the Streamlit app
st.set_page_config(
    page_title="Sales Dashboard",   # Title shown in browser tab
    page_icon="📊",                 # Icon shown in browser tab
    layout="wide"                  # Use full width layout
)

# ---------------------------
# Title Section
# ---------------------------
# Main heading of the dashboard
st.title("📊 Car Sales Analytics Dashboard")

# Small description below title
st.markdown("🚀 Real-time insights from Spring Boot APIs")

# ---------------------------
# API FUNCTIONS (Cached)
# ---------------------------
# @st.cache_data is used to cache API results
# This improves performance by avoiding repeated API calls

@st.cache_data
def get_yearly_sales():
    # Calling Spring Boot API for yearly sales data
    response = requests.get("http://localhost:8080/api/car-sales/yearly-count")
    
    # Convert JSON response into pandas DataFrame
    return pd.DataFrame(response.json()['data'])


@st.cache_data
def get_monthly_sales():
    # Calling API for monthly sales
    response = requests.get("http://localhost:8080/api/car-sales/monthly")
    
    # Convert JSON to DataFrame
    return pd.DataFrame(response.json()['data'])


@st.cache_data
def get_price_vs_year():
    # Calling API for price vs year analysis
    response = requests.get("http://localhost:8080/api/car-sales/price-year")
    
    # Convert JSON to DataFrame
    return pd.DataFrame(response.json()['data'])

# ---------------------------
# SIDEBAR SECTION
# ---------------------------
# Sidebar is used for filters and navigation
st.sidebar.header("📊 Dashboard Controls")

# Radio button to select analysis type
option = st.sidebar.radio(
    "Select Analysis",                   # Label
    ["Yearly Sales", "Monthly Sales", "Price Analysis"]  # Options
)

# ---------------------------
# YEARLY SALES DASHBOARD
# ---------------------------
# If user selects "Yearly Sales"
if option == "Yearly Sales":

    # Fetch yearly data
    df = get_yearly_sales()

    # Sidebar filters
    st.sidebar.subheader("🔍 Filters")

    # Get minimum and maximum year from data
    min_year = int(df['year'].min())
    max_year = int(df['year'].max())

    # Create a slider for selecting year range
    year_range = st.sidebar.slider(
        "Select Year Range",   # Label
        min_year,              # Minimum value
        max_year,              # Maximum value
        (min_year, max_year)   # Default selected range
    )

    # Filter data based on selected range
    df = df[(df['year'] >= year_range[0]) & (df['year'] <= year_range[1])]

    # ---------------------------
    # KPI Cards
    # ---------------------------
    # Creating 3 columns for metrics
    col1, col2, col3 = st.columns(3)

    # Display key metrics
    col1.metric("📅 Total Years", len(df))            # Total number of years
    col2.metric("📈 Max Sales", df['count'].max())   # Maximum sales value
    col3.metric("📉 Min Sales", df['count'].min())   # Minimum sales value

    # Horizontal line separator
    st.markdown("---")

    # ---------------------------
    # Line Chart (Trend)
    # ---------------------------
    fig = px.line(
        df,
        x="year",       # X-axis
        y="count",      # Y-axis
        markers=True,   # Show points
        text="count",   # Show values on chart
        title="📊 Sales Trend"
    )

    # Customize line and markers
    fig.update_traces(
        textposition="top center",   # Position of text
        line=dict(width=4),          # Line thickness
        marker=dict(size=10)         # Marker size
    )

    # Layout customization
    fig.update_layout(
        template="plotly_dark",  # Dark theme
        title_x=0.5,             # Center title
        hovermode="x unified"    # Unified hover tooltip
    )

    # Display chart
    st.plotly_chart(fig, use_container_width=True)

    # ---------------------------
    # Bar Chart
    # ---------------------------
    fig2 = px.bar(
        df,
        x="year",
        y="count",
        text="count",
        title="📊 Year-wise Sales"
    )

    # Apply dark theme
    fig2.update_layout(template="plotly_dark")

    # Show chart
    st.plotly_chart(fig2, use_container_width=True)

    # ---------------------------
    # Data Table
    # ---------------------------
    st.subheader("📋 Raw Data")

    # Display DataFrame
    st.dataframe(df, use_container_width=True)

# ---------------------------
# MONTHLY SALES DASHBOARD
# ---------------------------
elif option == "Monthly Sales":

    # Fetch monthly data
    df = get_monthly_sales()

    # Subheading
    st.subheader("📊 Monthly Sales Analysis")

    # Bar chart for monthly distribution
    fig = px.bar(
        df,
        x="month",
        y="count",
        color="count",   # Color based on values
        text="count",
        title="Monthly Sales Distribution"
    )

    # Layout settings
    fig.update_layout(template="plotly_dark", title_x=0.5)

    # Show chart
    st.plotly_chart(fig, use_container_width=True)

# ---------------------------
# PRICE ANALYSIS
# ---------------------------
elif option == "Price Analysis":

    # Fetch price data
    df = get_price_vs_year()

    # Subheading
    st.subheader("💰 Price vs Year Analysis")

    # Scatter plot for price trend
    fig = px.scatter(
        df,
        x="year",         # X-axis
        y="price",        # Y-axis
        size="price",     # Bubble size
        color="price",    # Color gradient
        hover_name="year",
        title="Price Growth Trend"
    )

    # Layout customization
    fig.update_layout(template="plotly_dark", title_x=0.5)

    # Display chart
    st.plotly_chart(fig, use_container_width=True)

# ---------------------------
# FOOTER
# ---------------------------
# Separator line
st.markdown("---")

# Footer text
st.caption("Built with Streamlit + Spring Boot | By Saif(CODEHUNT)")