param(
    [string]$BaseUrl = $(if ([string]::IsNullOrWhiteSpace($env:BAZAAR_API_URL)) { "http://localhost:8080/api" } else { $env:BAZAAR_API_URL }),
    [switch]$SkipDelete
)

$ErrorActionPreference = "Stop"

if (-not (Get-Command Invoke-WebRequest).Parameters.ContainsKey("SkipHttpErrorCheck")) {
    throw "This script requires PowerShell 7+ (pwsh) because it uses Invoke-WebRequest -SkipHttpErrorCheck."
}

$BaseUrl = $BaseUrl.Trim().TrimEnd("/")
if (-not $BaseUrl.ToLowerInvariant().EndsWith("/api")) {
    $BaseUrl = "$BaseUrl/api"
}

$stepResults = New-Object System.Collections.Generic.List[object]

function Add-StepResult {
    param(
        [string]$Step,
        [string]$Result,
        [string]$Details
    )

    $stepResults.Add([pscustomobject]@{
        step = $Step
        result = $Result
        details = $Details
    })
}

function Invoke-ApiJson {
    param(
        [string]$Method,
        [string]$Path,
        [object]$Body = $null,
        [int[]]$ExpectedStatusCodes = @(200)
    )

    $uri = "$BaseUrl$Path"
    $jsonBody = $null

    if ($null -ne $Body) {
        $jsonBody = $Body | ConvertTo-Json -Depth 10 -Compress
    }

    if ($null -ne $jsonBody) {
        $response = Invoke-WebRequest -Uri $uri -Method $Method -ContentType "application/json" -Body $jsonBody -SkipHttpErrorCheck
    } else {
        $response = Invoke-WebRequest -Uri $uri -Method $Method -SkipHttpErrorCheck
    }

    $statusCode = [int]$response.StatusCode
    if ($ExpectedStatusCodes -notcontains $statusCode) {
        throw "Unexpected HTTP status for $Method $uri. Expected $($ExpectedStatusCodes -join ', ') but got $statusCode. Body: $($response.Content)"
    }

    $data = $null
    if (-not [string]::IsNullOrWhiteSpace($response.Content)) {
        try {
            $data = $response.Content | ConvertFrom-Json
        } catch {
            $data = $response.Content
        }
    }

    return [pscustomobject]@{
        method = $Method
        uri = $uri
        statusCode = $statusCode
        data = $data
        rawBody = $response.Content
    }
}

$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$isoNow = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
$itemName = "P4_TEST_ITEM_$timestamp"

$itemId = $null
$snapshotId = $null
$orderId = $null
$tradeId = $null
$noteId = $null

try {
    Write-Host "Running Project 4 full system test against: $BaseUrl"

    $createItem = Invoke-ApiJson -Method "POST" -Path "/items" -Body @{
        name = $itemName
        category = "FARMING"
        rarity = "COMMON"
        npcSellPrice = 41.75
    }
    $itemId = [int]$createItem.data.id
    Add-StepResult -Step "Create Item" -Result "PASS" -Details "itemId=$itemId"

    Invoke-ApiJson -Method "GET" -Path "/items/$itemId" | Out-Null
    Add-StepResult -Step "Get Item By ID" -Result "PASS" -Details "itemId=$itemId"

    Invoke-ApiJson -Method "PUT" -Path "/items/$itemId" -Body @{
        itemId = $itemId
        name = "${itemName}_UPDATED"
        category = "MINING"
        rarity = "UNCOMMON"
        npcSellPrice = 52.25
    } | Out-Null
    Add-StepResult -Step "Update Item" -Result "PASS" -Details "itemId=$itemId"

    Invoke-ApiJson -Method "GET" -Path "/items/$itemId" | Out-Null
    Add-StepResult -Step "Get Updated Item" -Result "PASS" -Details "itemId=$itemId"

    $createSnapshot = Invoke-ApiJson -Method "POST" -Path "/price-snapshots" -Body @{
        itemId = $itemId
        buyPrice = 110.50
        sellPrice = 114.25
        buyVolume = 1200
        sellVolume = 900
        snapshotTime = $isoNow
    }
    $snapshotId = [int]$createSnapshot.data.id
    Add-StepResult -Step "Create Price Snapshot" -Result "PASS" -Details "snapshotId=$snapshotId"

    Invoke-ApiJson -Method "GET" -Path "/price-snapshots/$snapshotId" | Out-Null
    Add-StepResult -Step "Get Price Snapshot By ID" -Result "PASS" -Details "snapshotId=$snapshotId"

    Invoke-ApiJson -Method "PUT" -Path "/price-snapshots/$snapshotId" -Body @{
        snapshotId = $snapshotId
        itemId = $itemId
        buyPrice = 111.00
        sellPrice = 115.00
        buyVolume = 1250
        sellVolume = 910
        snapshotTime = (Get-Date).AddMinutes(1).ToString("yyyy-MM-ddTHH:mm:ss")
    } | Out-Null
    Add-StepResult -Step "Update Price Snapshot" -Result "PASS" -Details "snapshotId=$snapshotId"

    Invoke-ApiJson -Method "GET" -Path "/price-snapshots/$snapshotId" | Out-Null
    Add-StepResult -Step "Get Updated Price Snapshot" -Result "PASS" -Details "snapshotId=$snapshotId"

    $createOrder = Invoke-ApiJson -Method "POST" -Path "/orders" -Body @{
        itemId = $itemId
        orderType = "BUY"
        quantity = 128
        targetPrice = 99.50
        status = "OPEN"
        createdAt = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    }
    $orderId = [int]$createOrder.data.id
    Add-StepResult -Step "Create Order" -Result "PASS" -Details "orderId=$orderId"

    Invoke-ApiJson -Method "GET" -Path "/orders/$orderId" | Out-Null
    Add-StepResult -Step "Get Order By ID" -Result "PASS" -Details "orderId=$orderId"

    Invoke-ApiJson -Method "PUT" -Path "/orders/$orderId" -Body @{
        orderId = $orderId
        itemId = $itemId
        orderType = "BUY"
        quantity = 128
        targetPrice = 101.00
        status = "PARTIAL"
        createdAt = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    } | Out-Null
    Add-StepResult -Step "Update Order" -Result "PASS" -Details "orderId=$orderId"

    Invoke-ApiJson -Method "GET" -Path "/orders/$orderId" | Out-Null
    Add-StepResult -Step "Get Updated Order" -Result "PASS" -Details "orderId=$orderId"

    $createTrade = Invoke-ApiJson -Method "POST" -Path "/trades" -Body @{
        orderId = $orderId
        qtyFilled = 64
        fillPrice = 101.25
        fee = 1.25
        profit = 8.50
        tradeTime = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    }
    $tradeId = [int]$createTrade.data.id
    Add-StepResult -Step "Create Trade" -Result "PASS" -Details "tradeId=$tradeId"

    Invoke-ApiJson -Method "GET" -Path "/trades/$tradeId" | Out-Null
    Add-StepResult -Step "Get Trade By ID" -Result "PASS" -Details "tradeId=$tradeId"

    Invoke-ApiJson -Method "PUT" -Path "/trades/$tradeId" -Body @{
        tradeId = $tradeId
        orderId = $orderId
        qtyFilled = 64
        fillPrice = 101.50
        fee = 1.35
        profit = 9.10
        tradeTime = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    } | Out-Null
    Add-StepResult -Step "Update Trade" -Result "PASS" -Details "tradeId=$tradeId"

    Invoke-ApiJson -Method "GET" -Path "/trades/$tradeId" | Out-Null
    Add-StepResult -Step "Get Updated Trade" -Result "PASS" -Details "tradeId=$tradeId"

    $createNote = Invoke-ApiJson -Method "POST" -Path "/notes" -Body @{
        itemId = $itemId
        noteText = "Project 4 integration test note"
        createdAt = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    }
    $noteId = [int]$createNote.data.id
    Add-StepResult -Step "Create Note" -Result "PASS" -Details "noteId=$noteId"

    Invoke-ApiJson -Method "GET" -Path "/notes/$noteId" | Out-Null
    Add-StepResult -Step "Get Note By ID" -Result "PASS" -Details "noteId=$noteId"

    Invoke-ApiJson -Method "PUT" -Path "/notes/$noteId" -Body @{
        noteId = $noteId
        itemId = $itemId
        noteText = "Project 4 integration test note (updated)"
        createdAt = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
    } | Out-Null
    Add-StepResult -Step "Update Note" -Result "PASS" -Details "noteId=$noteId"

    Invoke-ApiJson -Method "GET" -Path "/notes/$noteId" | Out-Null
    Add-StepResult -Step "Get Updated Note" -Result "PASS" -Details "noteId=$noteId"

    if (-not $SkipDelete) {
        Invoke-ApiJson -Method "DELETE" -Path "/notes/$noteId" -ExpectedStatusCodes @(204) | Out-Null
        Add-StepResult -Step "Delete Note" -Result "PASS" -Details "noteId=$noteId"

        Invoke-ApiJson -Method "GET" -Path "/notes/$noteId" -ExpectedStatusCodes @(404) | Out-Null
        Add-StepResult -Step "Get Note After Delete" -Result "PASS" -Details "expected404=true"

        Invoke-ApiJson -Method "DELETE" -Path "/trades/$tradeId" -ExpectedStatusCodes @(204) | Out-Null
        Add-StepResult -Step "Delete Trade" -Result "PASS" -Details "tradeId=$tradeId"

        Invoke-ApiJson -Method "GET" -Path "/trades/$tradeId" -ExpectedStatusCodes @(404) | Out-Null
        Add-StepResult -Step "Get Trade After Delete" -Result "PASS" -Details "expected404=true"

        Invoke-ApiJson -Method "DELETE" -Path "/orders/$orderId" -ExpectedStatusCodes @(204) | Out-Null
        Add-StepResult -Step "Delete Order" -Result "PASS" -Details "orderId=$orderId"

        Invoke-ApiJson -Method "GET" -Path "/orders/$orderId" -ExpectedStatusCodes @(404) | Out-Null
        Add-StepResult -Step "Get Order After Delete" -Result "PASS" -Details "expected404=true"

        Invoke-ApiJson -Method "DELETE" -Path "/price-snapshots/$snapshotId" -ExpectedStatusCodes @(204) | Out-Null
        Add-StepResult -Step "Delete Price Snapshot" -Result "PASS" -Details "snapshotId=$snapshotId"

        Invoke-ApiJson -Method "GET" -Path "/price-snapshots/$snapshotId" -ExpectedStatusCodes @(404) | Out-Null
        Add-StepResult -Step "Get Price Snapshot After Delete" -Result "PASS" -Details "expected404=true"

        Invoke-ApiJson -Method "DELETE" -Path "/items/$itemId" -ExpectedStatusCodes @(204) | Out-Null
        Add-StepResult -Step "Delete Item" -Result "PASS" -Details "itemId=$itemId"

        Invoke-ApiJson -Method "GET" -Path "/items/$itemId" -ExpectedStatusCodes @(404) | Out-Null
        Add-StepResult -Step "Get Item After Delete" -Result "PASS" -Details "expected404=true"
    }

    $artifactDir = Join-Path $PSScriptRoot "..\artifacts"
    if (-not (Test-Path $artifactDir)) {
        New-Item -Path $artifactDir -ItemType Directory | Out-Null
    }

    $result = [ordered]@{
        generatedAt = (Get-Date).ToString("o")
        baseUrl = $BaseUrl
        skipDelete = [bool]$SkipDelete
        ids = [ordered]@{
            itemId = $itemId
            snapshotId = $snapshotId
            orderId = $orderId
            tradeId = $tradeId
            noteId = $noteId
        }
        steps = $stepResults
    }

    $artifactFile = Join-Path $artifactDir "project4-system-test-$timestamp.json"
    $result | ConvertTo-Json -Depth 20 | Out-File -FilePath $artifactFile -Encoding utf8

    Write-Host ""
    Write-Host "Project 4 system test completed successfully."
    Write-Host "Base URL: $BaseUrl"
    Write-Host "Artifact file: $artifactFile"
    Write-Host ""
    Write-Host "Step Summary"
    $stepResults | Format-Table -AutoSize
    Write-Host ""
    Write-Host "Created IDs (use in sql/project4_verification_queries.sql):"
    Write-Host "  itemId=$itemId"
    Write-Host "  snapshotId=$snapshotId"
    Write-Host "  orderId=$orderId"
    Write-Host "  tradeId=$tradeId"
    Write-Host "  noteId=$noteId"
}
catch {
    Add-StepResult -Step "Script Failure" -Result "FAIL" -Details $_.Exception.Message

    Write-Error "Project 4 system test failed. $($_.Exception.Message)"
    if ($stepResults.Count -gt 0) {
        Write-Host "Completed steps before failure:"
        $stepResults | Format-Table -AutoSize
    }

    exit 1
}
